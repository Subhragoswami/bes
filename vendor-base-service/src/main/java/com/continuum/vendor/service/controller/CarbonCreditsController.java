package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.service.CarbonCreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/carbon-credits")
public class CarbonCreditsController {

    private final CarbonCreditService carbonCreditService;
    @GetMapping
    public ResponseDto<BigDecimal> getTotalCarbonCredit(){
        log.info(" Getting total carbon credit for the Vendor");
        return carbonCreditService.getTotalCarbonCredit();
    }
}
