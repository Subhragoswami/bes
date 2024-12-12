package com.continuum.vendor.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse{
    private BigDecimal totalCarbonCreditGenerated;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime startDate;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private  LocalDateTime endDate;
    private List<VendorReportDetailsResponse> vendorReportResponses;
}
