package com.continuum.vendor.service.util;

import com.continuum.vendor.service.entity.vendor.FileData;
import com.continuum.vendor.service.exceptions.VendorException;
import com.continuum.vendor.service.repository.vendor.FilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvFileServiceUtil {
    private final FilesRepository filesRepository;
    public UUID downloadCSVFile(String headers, List<String> dataList, String fileName, String category, BigDecimal reportResponse, LocalDateTime startDate, LocalDateTime endDate, UUID id) {
        try {
            String generatedFileName = fileName + "_" + getCurrentDate() + ".csv";
            log.info("Preparing CSV file for : {}", generatedFileName);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            writeCSV(byteArrayOutputStream, dataList, headers, category, reportResponse, startDate, endDate);
            byte[] csvData = byteArrayOutputStream.toByteArray();
            FileData fileData = filesRepository.save(FileData.builder()
                    .type("Vendor_Admin_" + fileName)
                    .contentType("CSV")
                    .data(csvData)
                    .name(generatedFileName)
                    .identifier(id.toString())
                    .build());
            log.info("Carbon Credit CSV file stored successfully.");
            return  fileData.getId();
        } catch (Exception ex) {
            log.error("An error occurred while downloading the CSV file: {}", ex.getMessage());
            throw new VendorException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE);
        }
    }
    private static void writeCSV(ByteArrayOutputStream byteArrayOutputStream, List<String> dataList, String headers, String category, BigDecimal reportResponse, LocalDateTime startDate, LocalDateTime endDate){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            log.info("Writing csv for carbon credit report");
            writer.write("Category, " + category);
            writer.newLine();
            if(ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
                writer.write("Duration, " + startDate + " to " + endDate);
                writer.newLine();
            }
            writer.write("Total Carbon Credit Generated, " + reportResponse);
            writer.newLine();
            writer.newLine();
            writer.write(headers);
            writer.newLine();
            for (String vendorDatum : dataList) {
                writer.write(String.join("\t", vendorDatum));
                writer.newLine();
            }

        }catch (Exception e) {
            log.error("An error occurred while downloading the CSV file: {}", e.getMessage());
            throw new VendorException(ErrorConstants.REPORT_ERROR_CODE, ErrorConstants.REPORT_ERROR_MESSAGE);
        }
    }
    private static String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

}
