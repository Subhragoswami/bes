package com.continuum.vendor.service.controller;

import com.continuum.vendor.service.model.response.ChargingStationResponse;
import com.continuum.vendor.service.model.response.ChargingStationDetailsResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.model.request.ChargingStationFilterRequest;
import com.continuum.vendor.service.service.ChargingStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/charging-station")
public class ChargingStationController {

    private final ChargingStationService chargingStationService;
    @PostMapping
    public ResponseDto<ChargingStationResponse> getChargingStationDetails(@PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                                          @RequestParam(value = "search", required = false) String search,
                                                                          @RequestBody( required = false) ChargingStationFilterRequest chargingStationFilterRequest){
        log.info("Getting Charging Station details");
        return chargingStationService.getChargingStationDetails(pageable, search, chargingStationFilterRequest);
    }
    @GetMapping("/{id}")
    public ResponseDto<ChargingStationDetailsResponse> getChargingStationDetailsById(@PathVariable long id) {
        log.info("Getting Charging Station details for id:{}", id);
        return chargingStationService.getChargingStationDetails(id);
    }

    @GetMapping("/city")
    public ResponseDto<String> getCityList(){
        log.info("Request for getting cityList");
        return chargingStationService.getCityList();
    }
}
