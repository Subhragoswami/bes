package com.continum.vendor.service.service;

import com.continuum.vendor.service.dao.VendorDashboardDao;
import com.continuum.vendor.service.exceptions.VendorException;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.response.VendorDashboardResponse;
import com.continuum.vendor.service.service.VendorDashboardService;
import com.continuum.vendor.service.util.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class VendorDashboardServiceTest {

    @Mock
    private VendorDashboardDao vendorDashboardDao;
    @Mock
    private  VendorDashboardResponse vendorDashboardResponse;

    @InjectMocks
    private VendorDashboardService vendorDashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetVendorDashboardDetailsWithNullValues() {
        when(vendorDashboardDao.getCountOfTotalCharger()).thenReturn(0);
        when(vendorDashboardDao.getTotalEnergy()).thenReturn(null);
        when(vendorDashboardDao.getTotalUtilization()).thenReturn(null);
        when(vendorDashboardDao.getTotalSession()).thenReturn(0);
        when(vendorDashboardDao.getTotalCarbonCredit()).thenReturn(null);

        ResponseDto<VendorDashboardResponse> response = vendorDashboardService.getVendorDashboardDetails();

        assertNotNull(response);
        assertEquals(AppConstants.RESPONSE_SUCCESS, response.getStatus());
        assertEquals(1, response.getData().size());

        VendorDashboardResponse dashboardResponse = response.getData().get(0);
        assertEquals(0, dashboardResponse.getTotalDevice());
        assertNull(dashboardResponse.getTotalEnergy());
        assertNull(dashboardResponse.getTotalUtilization());
        assertEquals(0, dashboardResponse.getTotalSessions());
        assertNull(dashboardResponse.getTotalCarbonCredit());
    }

    @Test
    void testGetVendorDashboardDetailsDaoException() {
        when(vendorDashboardDao.getCountOfTotalCharger()).thenThrow(new VendorException("ERROR_CODE", "DAO error"));

        VendorException exception = assertThrows(VendorException.class, () -> {
            vendorDashboardService.getVendorDashboardDetails();
        });

        assertEquals("DAO error", exception.getErrorMessage());
    }
}