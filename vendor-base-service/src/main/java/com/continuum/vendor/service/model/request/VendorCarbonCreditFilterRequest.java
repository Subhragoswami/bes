package com.continuum.vendor.service.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorCarbonCreditFilterRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
