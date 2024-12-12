package com.continuum.vendor.service.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class VendorReportFilterRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
