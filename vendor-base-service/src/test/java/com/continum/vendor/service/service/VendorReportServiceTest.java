package com.continum.vendor.service.service;

import com.continuum.vendor.service.dao.CarbonCreditsDao;
import com.continuum.vendor.service.entity.vendor.VendorReport;
import com.continuum.vendor.service.exceptions.ValidationException;
import com.continuum.vendor.service.mapper.VendorReportMapper;
import com.continuum.vendor.service.model.request.VendorReportFilterRequest;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.response.VendorFilterResponse;
import com.continuum.vendor.service.service.VendorReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VendorReportServiceTest {

    @Mock
    private CarbonCreditsDao carbonCreditsDao;

    @Mock
    private VendorReportMapper vendorReportMapper;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private VendorReportFilterRequest vendorReportFilterRequest;

    @InjectMocks
    private VendorReportService vendorReportService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        vendorReportFilterRequest =new VendorReportFilterRequest();
    }

    @Test
    public void testGetAllReportsData() throws JsonProcessingException, ParseException {
        Pageable pageable = Pageable.unpaged();

        vendorReportFilterRequest.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        vendorReportFilterRequest.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59, 59));

        VendorFilterResponse vendorReportsResponse = new VendorFilterResponse();
        vendorReportsResponse.setId(UUID.randomUUID());
        vendorReportsResponse.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        vendorReportsResponse.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59, 59));
        VendorReport vendorReport = new VendorReport();
        vendorReport.setId(UUID.randomUUID());
        vendorReport.setFilter(String.valueOf(vendorReportFilterRequest));

        Page<VendorReport> mockPage = new PageImpl<>(Collections.singletonList(vendorReport));
        when(carbonCreditsDao.getAllReportDetails(any(), any(), any())).thenReturn(mockPage);

        when(vendorReportMapper.mapToVendorReport(any())).thenReturn(vendorReportsResponse);

        when(mapper.readValue(any(String.class), any(Class.class))).thenReturn(vendorReportFilterRequest);

        ResponseDto<VendorFilterResponse> responseDto = vendorReportService.getAllReportsData(pageable, vendorReportFilterRequest);

        assertEquals(1, responseDto.getData().size());
    }

    @Test
    public void testGetAllReportsData_NoDataFound() throws ParseException {
        when(carbonCreditsDao.getAllReportDetails(any(), any(), any())).thenReturn(Page.empty());
        ResponseDto<VendorFilterResponse> responseDto = vendorReportService.getAllReportsData(Pageable.unpaged(), new VendorReportFilterRequest());
        assertEquals(0, responseDto.getData().size());
    }
    @Test
    public void testGetAllReportsData_InvalidDateRange() {
        VendorReportFilterRequest filterRequest = new VendorReportFilterRequest();
        filterRequest.setStartDate(LocalDateTime.of(2023, 12, 31, 23, 59, 59));
        filterRequest.setEndDate(LocalDateTime.of(2023, 1, 1, 0, 0));

        assertThrows(ValidationException.class, () -> {
            vendorReportService.getAllReportsData(Pageable.unpaged(), filterRequest);
        });
    }
    @Test
    public void testGetAllReportsData_WithPagination() throws JsonProcessingException, ParseException {
        Pageable pageable = PageRequest.of(0, 10);

        VendorReportFilterRequest filterRequest = new VendorReportFilterRequest();
        filterRequest.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        filterRequest.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59, 59));

        VendorFilterResponse vendorReportsResponse = new VendorFilterResponse();
        vendorReportsResponse.setId(UUID.randomUUID());
        vendorReportsResponse.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        vendorReportsResponse.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59, 59));
        VendorReport vendorReport = new VendorReport();
        vendorReport.setId(UUID.randomUUID());
        vendorReport.setFilter(String.valueOf(filterRequest));

        Page<VendorReport> mockPage = new PageImpl<>(Collections.singletonList(vendorReport));
        when(carbonCreditsDao.getAllReportDetails(any(), any(), any())).thenReturn(mockPage);

        when(vendorReportMapper.mapToVendorReport(any())).thenReturn(vendorReportsResponse);

        when(mapper.readValue(any(String.class), any(Class.class))).thenReturn(filterRequest);

        ResponseDto<VendorFilterResponse> responseDto = vendorReportService.getAllReportsData(pageable, filterRequest);

        assertEquals(1, responseDto.getData().size());
    }

    @Test
    public void testGetAllReportsData_NoFiltersApplied() throws JsonProcessingException, ParseException {
        Pageable pageable = Pageable.unpaged();

        VendorReportFilterRequest filterRequest = new VendorReportFilterRequest();

        VendorFilterResponse vendorReportsResponse = new VendorFilterResponse();
        vendorReportsResponse.setId(UUID.randomUUID());
        VendorReport vendorReport = new VendorReport();
        vendorReport.setId(UUID.randomUUID());
        vendorReport.setFilter(String.valueOf(filterRequest));

        Page<VendorReport> mockPage = new PageImpl<>(Collections.singletonList(vendorReport));
        when(carbonCreditsDao.getAllReportDetails(any(), any(), any())).thenReturn(mockPage);

        when(vendorReportMapper.mapToVendorReport(any())).thenReturn(vendorReportsResponse);

        when(mapper.readValue(any(String.class), any(Class.class))).thenReturn(filterRequest);

        ResponseDto<VendorFilterResponse> responseDto = vendorReportService.getAllReportsData(pageable, filterRequest);

        assertEquals(1, responseDto.getData().size());
    }

    @Test
    public void testGetAllReportsData_MapperException() throws JsonProcessingException, ParseException {
        Pageable pageable = Pageable.unpaged();

        VendorReportFilterRequest filterRequest = new VendorReportFilterRequest();
        filterRequest.setStartDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        filterRequest.setEndDate(LocalDateTime.of(2023, 12, 31, 23, 59, 59));

        VendorReport vendorReport = new VendorReport();
        vendorReport.setId(UUID.randomUUID());
        vendorReport.setFilter("Invalid JSON String");

        Page<VendorReport> mockPage = new PageImpl<>(Collections.singletonList(vendorReport));
        when(carbonCreditsDao.getAllReportDetails(any(), any(), any())).thenReturn(mockPage);

        when(mapper.readValue(any(String.class), any(Class.class))).thenThrow(new JsonProcessingException("Error processing JSON") {});

        assertThrows(RuntimeException.class, () -> {
            vendorReportService.getAllReportsData(pageable, filterRequest);
        });
    }
}
