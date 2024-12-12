package com.continuum.vendor.service.service;

import com.continuum.vendor.service.dao.CarbonCreditsDao;
import com.continuum.vendor.service.entity.vendor.CarbonCredits;
import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.entity.vendor.VendorReport;
import com.continuum.vendor.service.exceptions.VendorException;
import com.continuum.vendor.service.mapper.VendorReportMapper;
import com.continuum.vendor.service.model.request.VendorCarbonCreditFilterRequest;
import com.continuum.vendor.service.model.request.VendorReportFilterRequest;
import com.continuum.vendor.service.model.response.*;
import com.continuum.vendor.service.repository.vendor.FilesRepository;
import com.continuum.vendor.service.util.*;
import com.continuum.vendor.service.util.enums.Category;
import com.continuum.vendor.service.util.enums. Status;
import com.continuum.vendor.service.validator.ReportValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.continuum.vendor.service.util.AppConstants.RESPONSE_SUCCESS;
import static com.continuum.vendor.service.util.ErrorConstants.REPORT_ERROR_MESSAGE;


@Slf4j
@Service
@RequiredArgsConstructor
public class VendorReportService {
    private final CarbonCreditsDao carbonCreditsDao;
    private final ObjectMapper mapper;
    private final VendorReportMapper vendorReportMapper;
    private final CsvRecordsUtil csvRecordsUtil;
    private final FilesRepository filesRepository;
    private final CsvFileServiceUtil csvFileServiceUtil;

    public CompletableFuture<ResponseDto<String>> getCarbonCreditDetails(VendorCarbonCreditFilterRequest vendorCarbonCreditFilterRequest, UUID userId, UUID vendorId, Optional<UUID> reportId) throws ParseException, JsonProcessingException {
        log.info("Initiating retrieval of carbon credit details for userId", vendorCarbonCreditFilterRequest);
        LocalDateTime formattedStartDate = null;
        LocalDateTime formattedEndDate = null;
        if (ObjectUtils.isNotEmpty(vendorCarbonCreditFilterRequest) && ObjectUtils.isNotEmpty(vendorCarbonCreditFilterRequest.getStartDate()) && ObjectUtils.isNotEmpty(vendorCarbonCreditFilterRequest.getEndDate())) {
            formattedStartDate = vendorCarbonCreditFilterRequest.getStartDate();
            formattedEndDate = vendorCarbonCreditFilterRequest.getEndDate()
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59);
        }
        ReportValidator.reportRequestIdAndDateValidation(formattedStartDate, formattedEndDate, userId, vendorId);
        VendorReport vendorReport = buildSaveVendorReports((getFilterString(formattedStartDate, formattedEndDate)), userId, vendorId, Category.CARBON_CREDIT.getName(), Status.INITIATED.getName());
        vendorReport = carbonCreditsDao.save(vendorReport);

        processAndGenerateReport(formattedStartDate, formattedEndDate, vendorReport);

        return CompletableFuture.completedFuture(ResponseDto.<String>builder()
                .status(RESPONSE_SUCCESS)
                .data(List.of("Report generation initiated"))
                .build());
    }

    @Async
    public CompletableFuture<Void> processAndGenerateReport(LocalDateTime formattedStartDate, LocalDateTime formattedEndDate, VendorReport vendorReport) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Processing and generating report for Vendor Report ID: {}", vendorReport.getId());
                List<Object[]> sessionWithCreditsList = carbonCreditsDao.findSessionsWithCreditsByDate(formattedStartDate, formattedEndDate);
                log.info("Retrieved {} charging sessions with credits", sessionWithCreditsList.size());

                List<String> encryptedEmails = sessionWithCreditsList.stream()
                        .filter(obj -> ObjectUtils.isNotEmpty(((ChargingSession) obj[0]).getVendorCustomer()))
                        .map(obj -> {
                            String email = ((ChargingSession) obj[0]).getVendorCustomer().getEmail();
                            if (email == null) {
                                System.out.println("Email is null for vendor customer in session: " + obj[0]);
                            }
                            return email;
                        })
                        .filter(ObjectUtils::isNotEmpty)
                        .collect(Collectors.toList());

                List<String> encryptedPhones = sessionWithCreditsList.stream()
                        .filter(obj -> ObjectUtils.isNotEmpty(((ChargingSession) obj[0]).getVendorCustomer()))
                        .map(obj -> ((ChargingSession) obj[0]).getVendorCustomer().getMobile())
                        .collect(Collectors.toList());
                log.info("Starting decryption process for email and phone data");
                Map<String, List<String>> decryptedEmails = EncryptionUtil.bulkDecryptParallel(encryptedEmails);
                Map<String, List<String>> decryptedPhones = EncryptionUtil.bulkDecryptParallel(encryptedPhones);
                log.info("Decryption process completed for all data");
                List<VendorReportDetailsResponse> vendorReportDetailsResponseList = sessionWithCreditsList.parallelStream()
                        .map(obj -> {
                            ChargingSession chargingSession = (ChargingSession) obj[0];

                            CarbonCredits carbonCredits = chargingSession.getCarbonCredits();
                            BigDecimal carbonCredit = (carbonCredits != null) ? carbonCredits.getCreditScorePoints() : BigDecimal.ZERO;
                            VendorReportDetailsResponse vendorReportDetailsResponse = vendorReportMapper.mapToVendorReportDetailsResponse(chargingSession, carbonCredits, decryptedEmails, decryptedPhones);
                            vendorReportDetailsResponse.setCarbonCreditGenerated(carbonCredit);
                            return vendorReportDetailsResponse;
                        })
                        .collect(Collectors.toList());

                BigDecimal totalCarbonCredit = vendorReportDetailsResponseList.stream()
                        .map(VendorReportDetailsResponse::getCarbonCreditGenerated)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                log.info("Total carbon credit calculated: {}", totalCarbonCredit);
                ReportResponse reportResponse = buildVendorDetails(vendorReportDetailsResponseList, totalCarbonCredit);

                carbonCreditsDao.updateReportStatusById(vendorReport.getId(), Status.PROCESSING.getName(), null);
                String headers = "Session ID, Started at, Stopped at, Latitude, Longitude, Location, Vehicle Name, Vehicle Number, Energy Utilized (Kwh), Carbon Credits, Charging station name, User name, Email, Phone Number";
                List<VendorReportDetailsResponse> carbonCreditDetailsResponses = new ArrayList<>();
                List<String> csvContent = new ArrayList<>();
                for (VendorReportDetailsResponse vendorReportDetailsResponse : reportResponse.getVendorReportResponses()) {
                    carbonCreditDetailsResponses.add(vendorReportDetailsResponse);
                }
                csvRecordsUtil.getVendorCarbonCreditCsv(csvContent, carbonCreditDetailsResponses);
                log.info("formatted all csv contents");
                String fileId = String.valueOf(csvFileServiceUtil.downloadCSVFile(headers, csvContent, "carbon_credit", "carbonCredit", totalCarbonCredit, formattedStartDate, formattedEndDate, vendorReport.getId()));
                carbonCreditsDao.updateReportStatusById(vendorReport.getId(), Status.AVAILABLE.getName(), UUID.fromString(fileId));
            } catch (Exception e) {
                log.error("Error processing carbon credits and generating report", e);
                carbonCreditsDao.updateReportStatusById(vendorReport.getId(), Status.FAILED.getName(), null);
                throw new VendorException(ErrorConstants.REPORT_ERROR_CODE, REPORT_ERROR_MESSAGE);
            }
        });
    }
    public ResponseDto<VendorFilterResponse> getAllReportsData(Pageable pageable , VendorReportFilterRequest vendorReportFilterRequest) throws ParseException {
        Date formattedStartDate = null;
        Date formattedEndDate = null;
        if(ObjectUtils.isNotEmpty(vendorReportFilterRequest) && ObjectUtils.isNotEmpty(vendorReportFilterRequest.getStartDate()) && ObjectUtils.isNotEmpty(vendorReportFilterRequest.getEndDate())) {
            formattedStartDate = DateUtil.dateFormat(vendorReportFilterRequest.getStartDate());
            formattedEndDate = DateUtil.dateFormat(vendorReportFilterRequest.getEndDate());
            formattedEndDate = adjustEndDate(formattedEndDate);
        }
        log.info("Retrieving all report data with startDate: {}, endDate: {}", formattedStartDate, formattedEndDate);
        ReportValidator.reportRequestDateValidation(formattedStartDate, formattedEndDate);
        Page<VendorReport> vendorReports = carbonCreditsDao.getAllReportDetails(pageable, formattedStartDate, formattedEndDate);
        List<VendorFilterResponse> vendorReportResponses = new ArrayList<>();
        log.info("Processing vendor report , Total reports found: {}",  vendorReports.getTotalElements());
        for(VendorReport vendorReport : vendorReports) {
            VendorFilterResponse vendorReportResponse = vendorReportMapper.mapToVendorReport(vendorReport);
            vendorReportFilterRequest = getFilterObject(vendorReport.getFilter());
            if(ObjectUtils.isNotEmpty(vendorReportFilterRequest.getStartDate()) && ObjectUtils.isNotEmpty(vendorReportFilterRequest.getEndDate())) {
                vendorReportResponse.setStartDate(vendorReportFilterRequest.getStartDate());
                vendorReportResponse.setEndDate(vendorReportFilterRequest.getEndDate());
            }
            vendorReportResponses.add(vendorReportResponse);
        }
        return ResponseDto.<VendorFilterResponse>builder()
                .status(RESPONSE_SUCCESS)
                .count(vendorReportResponses.stream().count())
                .total(vendorReports.getTotalElements())
                .data(vendorReportResponses)
                .build();
    }
    private VendorReportFilterRequest getFilterObject(String filter){
        try {
            return mapper.readValue(filter, VendorReportFilterRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON for filter string", e);
        }
    }
    private String getFilterString(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Map<String, LocalDateTime> dateMap = new HashMap<>();
            dateMap.put("startDate", startDate);
            dateMap.put("endDate", endDate);
            return mapper.writeValueAsString(dateMap);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for filter string with startDate: {}, endDate: {}", startDate, endDate, e);
            throw new RuntimeException("Error processing JSON for filter string", e);
        }
    }
    private VendorReport buildSaveVendorReports(String filter, UUID userId, UUID vendorId, String type, String status){
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = currentDate.atTime(LocalTime.now());
        ZonedDateTime zonedDateTime = currentDateTime.atZone(ZoneId.systemDefault());
        return VendorReport.builder()
                .userId(userId)
                .dateCreated(Date.from(zonedDateTime.toInstant()))
                .vendorId(vendorId)
                .type(type)
                .status(status)
                .filter(filter)
                .build();
    }
    private Date adjustEndDate(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    private ReportResponse buildVendorDetails(List<VendorReportDetailsResponse> vendorReportDetailsResponse, BigDecimal totalCarbonCredit){
        return ReportResponse.builder()
                .vendorReportResponses(vendorReportDetailsResponse)
                .totalCarbonCreditGenerated(totalCarbonCredit)
                .build();
    }
}

