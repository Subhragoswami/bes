package com.continuum.vendor.service.service;

import com.continuum.vendor.service.dao.VendorDashboardDao;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.response.VendorDashboardResponse;
import com.continuum.vendor.service.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class VendorDashboardService {
    private final VendorDashboardDao vendorAdminDao;
    private final VendorDashboardResponse vendorAdminResponse;
    public ResponseDto<VendorDashboardResponse> getVendorDashboardDetails(){
        log.info("mapping data to vendor dashboard response");
        setDashboardResponseData();
        log.info("Mapping data to vendor dashboard response");
        return ResponseDto.<VendorDashboardResponse>builder()
                .status(AppConstants.RESPONSE_SUCCESS)
                .data(List.of(vendorAdminResponse))
                .build();
    }
    private void setDashboardResponseData(){
        vendorAdminResponse.setTotalDevice(vendorAdminDao.getCountOfTotalCharger());
        vendorAdminResponse.setTotalEnergy(vendorAdminDao.getTotalEnergy());
        vendorAdminResponse.setTotalUtilization(vendorAdminDao.getTotalUtilization());
        vendorAdminResponse.setTotalSessions(vendorAdminDao.getTotalSession());
        vendorAdminResponse.setTotalCarbonCredit(vendorAdminDao.getTotalCarbonCredit());
    }
}
