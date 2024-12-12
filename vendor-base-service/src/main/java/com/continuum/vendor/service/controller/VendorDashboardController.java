package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.response.VendorDashboardResponse;
import com.continuum.vendor.service.service.VendorDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class VendorDashboardController {
    private final VendorDashboardService getVendorAdminDetails;
    @GetMapping
    public ResponseDto<VendorDashboardResponse> getVendorAdminDashboard(){
        log.info("Recieved request to get dashboard details");
        return getVendorAdminDetails.getVendorDashboardDetails();
    }

}
