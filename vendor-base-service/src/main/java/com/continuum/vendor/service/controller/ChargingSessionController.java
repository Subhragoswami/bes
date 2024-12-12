package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.request.ChargingSessionFilter;
import com.continuum.vendor.service.model.response.ChargingSessionResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.service.ChargingSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/charging-session")
public class ChargingSessionController {
    private final ChargingSessionService chargingSessionService;
    @PostMapping
    public ResponseDto<ChargingSessionResponse> getChargingSessionDetails(
            @RequestBody(required = false) ChargingSessionFilter chargingSessionFilter,
            @RequestParam(required = false) String search,
            @PageableDefault(sort = {"startAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Request received to get charging session by search: {}, filter : {}",search, chargingSessionFilter);
        return chargingSessionService.getChargingSessionDetails(chargingSessionFilter, search, pageable);
    }


}
