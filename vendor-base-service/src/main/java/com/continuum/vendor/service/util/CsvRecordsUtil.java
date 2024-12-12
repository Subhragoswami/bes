package com.continuum.vendor.service.util;

import com.continuum.vendor.service.model.response.VendorReportDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvRecordsUtil {
    private static final int DECIMAL_PLACES = 10;

    public void getVendorCarbonCreditCsv(List<String> csvContent, List<VendorReportDetailsResponse> carbonCreditDetailsResponses) {
        log.info("Got request for CSV format");
        for (VendorReportDetailsResponse carbonCreditDetailsResponse : carbonCreditDetailsResponses) {
            BigDecimal carbonCredit = carbonCreditDetailsResponse.getCarbonCreditGenerated().setScale(DECIMAL_PLACES, BigDecimal.ROUND_HALF_UP);
            String format = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                    carbonCreditDetailsResponse.getCustomerTransactionId(),
                    carbonCreditDetailsResponse.getStartAt(),
                    carbonCreditDetailsResponse.getStopAt(),
                    carbonCreditDetailsResponse.getLatitude(),
                    carbonCreditDetailsResponse.getLongitude(),
                    carbonCreditDetailsResponse.getLocation(),
                    carbonCreditDetailsResponse.getVehicleName(),
                    carbonCreditDetailsResponse.getVehicleNumber(),
                    carbonCreditDetailsResponse.getUsedEnergy(),
                    carbonCredit.toPlainString(),
                    carbonCreditDetailsResponse.getStationName(),
                    carbonCreditDetailsResponse.getUserName(),
                    carbonCreditDetailsResponse.getEmail(),
                    carbonCreditDetailsResponse.getPhoneNumber());
            csvContent.add(format);
        }
    }
    private String nullToString(Object obj) {
        return (obj == null || "null".equalsIgnoreCase(obj.toString().trim())) ? "" : obj.toString();
    }
}
