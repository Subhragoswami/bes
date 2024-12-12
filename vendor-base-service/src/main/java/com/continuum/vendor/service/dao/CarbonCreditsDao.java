package com.continuum.vendor.service.dao;

import com.continuum.vendor.service.entity.vendor.CarbonCredits;
import com.continuum.vendor.service.mapper.CarbonCreditMapper;
import com.continuum.vendor.service.repository.cms.CarbonCreditsRepositoryCMS;
import com.continuum.vendor.service.repository.vendor.CarbonCreditsRepository;
import com.continuum.vendor.service.entity.vendor.VendorReport;
import com.continuum.vendor.service.repository.vendor.ChargingSessionRepository;
import com.continuum.vendor.service.repository.vendor.VendorReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarbonCreditsDao {
    private final CarbonCreditsRepository carbonCreditsRepository;

    private final CarbonCreditsRepositoryCMS carbonCreditsRepositoryCMS;

    private final CarbonCreditMapper carbonCreditMapper;
    private final VendorReportRepository vendorReportRepository;
    public void saveCarbonCredits(String vendorCode, List<CarbonCredits> carbonCreditsList){
         carbonCreditsRepository.saveAll(carbonCreditsList);

         //Save to CMS
        carbonCreditsRepositoryCMS.saveAll(carbonCreditMapper.mapCarbonCreditList(carbonCreditsList, vendorCode));
    }

    public void updateReportStatusById(UUID id, String status, UUID fileId){
        log.info("Updating report status for report ID: {}, status: {}, fileId: {}", id, status, fileId);
        vendorReportRepository.updateReportStatusById(id, status, fileId);
    }

    private final ChargingSessionRepository chargingSessionRepository;
    public VendorReport save(VendorReport vendorReport){
        return vendorReportRepository.save(vendorReport);
    }
    public Optional<VendorReport> getDetailsById(UUID id){
        return vendorReportRepository.findById(id);
    }
    public boolean checkByReportId(Optional<UUID> reportId) {
        return reportId.isPresent() && vendorReportRepository.existsById(reportId.get());
    }

    public Page<VendorReport> getAllReportDetails(Pageable pageable, Date startDate, Date endDate){
        log.info("Retrieving all report details with startDate: {}, endDate: {}", startDate, endDate);
        if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            return vendorReportRepository.findReportByDate(pageable, startDate, endDate);
        }
        return vendorReportRepository.findAll(pageable);
    }
    public List<Object[]> findSessionsWithCreditsByDate(LocalDateTime startDate, LocalDateTime endDate){
        log.info("Finding sessions with credits by date range startDate: {}, endDate: {}", startDate, endDate);
        if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            return chargingSessionRepository.findAllSessionsWithCarbonCreditsByDate(startDate, endDate);
        } else {
            return chargingSessionRepository.findAllSessionsWithCarbonCredits();
        }
    }

    public BigDecimal getTotalCarbonCredit() {
        return carbonCreditsRepository.getTotalCC();
    }
}
