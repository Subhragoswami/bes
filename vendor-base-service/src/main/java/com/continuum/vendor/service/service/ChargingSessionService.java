package com.continuum.vendor.service.service;


import com.continuum.vendor.service.dao.ChargingSessionDao;
import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.model.request.ChargingSessionFilter;
import com.continuum.vendor.service.model.response.ChargingSessionResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.mapper.ChargingSessionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static com.continuum.vendor.service.util.AppConstants.RESPONSE_SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargingSessionService {
    private final ChargingSessionDao chargingSessionDao;
    private final ChargingSessionMapper sessionMapper;

    public ResponseDto<ChargingSessionResponse> getChargingSessionDetails(ChargingSessionFilter chargingSessionFilter, String search, Pageable pageable) {
        log.info("Fetching charging session details with criteria : {}", chargingSessionFilter);
        Page<ChargingSession> chargingSessionsList = chargingSessionDao.getAllChargingSession(chargingSessionFilter, search, pageable);
        log.info("Successfully fetched charging session details.");
        return ResponseDto.<ChargingSessionResponse>builder()
                .data(sessionMapper.mapToChargingSessionList(chargingSessionsList.getContent()))
                .status(RESPONSE_SUCCESS)
                .count(chargingSessionsList.stream().count())
                .total(chargingSessionsList.getTotalElements())
                .build();
    }
}
