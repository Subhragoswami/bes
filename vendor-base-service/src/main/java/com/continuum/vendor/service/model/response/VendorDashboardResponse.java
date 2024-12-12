package com.continuum.vendor.service.model.response;


import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class VendorDashboardResponse {
    private Integer totalDevice;
    private BigDecimal totalEnergy;
    private BigDecimal totalUtilization;
    private Integer totalSessions;
    private Integer activeCharging;
    private Integer onlineDevices;
    private Integer offlineDevices;
    private Integer idleDevice;
    private BigDecimal totalCarbonCredit;
    private Integer noOfAdmins;
}
