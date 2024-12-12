package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.request.VendorCarbonCreditFilterRequest;
import com.continuum.vendor.service.model.request.VendorReportFilterRequest;
import com.continuum.vendor.service.model.response.*;
import com.continuum.vendor.service.service.VendorReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/report")
public class VendorReportController {
    private final VendorReportService vendorReportService;
    @PostMapping
    public CompletableFuture<ResponseDto<String>> getCarbonCreditDetails(@RequestBody(required = false) VendorCarbonCreditFilterRequest vendorCarbonCreditFilterRequest,
                                                                         @RequestParam UUID userId,
                                                                         @RequestParam UUID vendorId) throws ParseException, JsonProcessingException {
        log.info("Received request to get vendor carbonCredit Report details");
        return vendorReportService.getCarbonCreditDetails(vendorCarbonCreditFilterRequest, userId, vendorId, Optional.empty());
    }

    @PostMapping("/list")
    public ResponseDto<VendorFilterResponse> getAllReports(@PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                           @RequestBody(required = false) VendorReportFilterRequest vendorReportFilterRequest) throws ParseException {
        log.info("Received request to get vendor Report details");
        return vendorReportService.getAllReportsData(pageable, vendorReportFilterRequest);
    }
}
