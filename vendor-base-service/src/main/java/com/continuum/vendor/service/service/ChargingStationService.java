package com.continuum.vendor.service.service;

import com.continuum.vendor.service.dao.ChargingStationDao;
import com.continuum.vendor.service.entity.vendor.ChargerDetails;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.entity.vendor.ConnectorDetails;
import com.continuum.vendor.service.entity.vendor.EVSEDetails;
import com.continuum.vendor.service.exceptions.ValidationException;
import com.continuum.vendor.service.mapper.ChargingStationMapper;
import com.continuum.vendor.service.model.response.*;
import com.continuum.vendor.service.model.request.ChargingStationFilterRequest;
import com.continuum.vendor.service.util.AppConstants;
import com.continuum.vendor.service.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static com.continuum.vendor.service.util.ErrorConstants.NOT_VALID_ERROR_CODE;
import static com.continuum.vendor.service.util.ErrorConstants.NOT_VALID_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChargingStationService {

    private final ChargingStationDao chargingStationDao;
    private final ChargingStationMapper chargingStationMapper;
    public ResponseDto<ChargingStation> getChargingStationData(UUID chargingStationDetailsId) {
        ChargingStation chargingStation = chargingStationDao.getChargingStationData(chargingStationDetailsId).
                orElseThrow(()-> new ValidationException(NOT_VALID_ERROR_CODE, NOT_VALID_ERROR_MESSAGE));
        return ResponseDto.<ChargingStation>builder()
                .status(AppConstants.RESPONSE_SUCCESS)
                .data(List.of(chargingStation))
                .build();
    }

    public ResponseDto<ChargingStationResponse> getChargingStationDetails(Pageable pageable, String search, ChargingStationFilterRequest chargingStationFilterRequest) {
        log.info("Fetching charging station details with search: {} and filter: {}", search, chargingStationFilterRequest);
        Page<ChargingStation> chargingStationDetails = chargingStationDao.getAllDetails(pageable, search, chargingStationFilterRequest);
        log.info("Fetched {} charging stations from database", chargingStationDetails.getTotalElements());
        List<ChargingStationResponse> chargingStationResponseList = chargingStationMapper.mapChargingStationToResponse(chargingStationDetails, ChargingStationResponse.class);
        log.info("Mapped charging station details to response DTOs");
        return ResponseDto.<ChargingStationResponse>builder()
                .status(AppConstants.RESPONSE_SUCCESS)
                .data(chargingStationResponseList)
                .count(chargingStationDetails.stream().count())
                .total(chargingStationDetails.getTotalElements())
                .build();
    }

    public ResponseDto<ChargingStationDetailsResponse> getChargingStationDetails(long id) {
        log.info("Fetch charging station by stationId : {}", id);
        ChargingStation chargingStationDetails = chargingStationDao.findByLocatonId(id).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "location id")));
        log.info("Successfully fetched charging station details for stationId: {}", id);
        ChargingStationDetailsResponse chargingStationUserResponse =setChargingstationUserResponse(chargingStationDetails);
        return ResponseDto.<ChargingStationDetailsResponse>builder()
                .status(AppConstants.RESPONSE_SUCCESS)
                .data(List.of(chargingStationUserResponse))
                .build();
    }

    public ResponseDto<String> getCityList(){
        log.info("getting cityList");
        List<String> chargingStations = chargingStationDao.getCityList();
        log.info("Successfully fetched city list");
        return ResponseDto.<String>builder()
                .status(AppConstants.RESPONSE_SUCCESS)
                .data((chargingStations))
                .build();
    }

    private ChargingStationDetailsResponse setChargingstationUserResponse(ChargingStation chargingStationDetails){
        log.info("Building charging station user response for locationId: {}", chargingStationDetails.getLocationId());
        List<EVSEDetails> evseDetailsList = new ArrayList<>();
        List<ConnectorDetails> connectorDetailsList= new ArrayList<>();
        BigDecimal carbonCredits = chargingStationDao.getTotalCC(chargingStationDetails.getLocationId());
        log.info("Total carbon credits for locationId {}: {}", chargingStationDetails.getLocationId(), carbonCredits);
        List<ChargerDetails> chargerDetailsList = chargingStationDao.getByLocationId(chargingStationDetails.getLocationId());
        for(ChargerDetails chargerDetails : chargerDetailsList) {
            List<EVSEDetails> evseDetails = chargingStationDao.getByChargerId(chargerDetails.getChargerId());
            for(EVSEDetails evseDetail : evseDetails) {
                List<ConnectorDetails> connectorDetails = chargingStationDao.getByEVSEId(evseDetail.getEvseId());
                connectorDetailsList.addAll(connectorDetails);
            }
            evseDetailsList.addAll(evseDetails);
        }
        ChargingStationDetailsResponse chargingStationResponse = chargingStationMapper.mapToChargingStationToResponse(chargingStationDetails, chargerDetailsList, evseDetailsList, connectorDetailsList);
        chargingStationResponse.setCarbonCreditGenerated(carbonCredits);
        log.info("Successfully built charging station user response for locationId: {}", chargingStationDetails.getLocationId());
        return chargingStationResponse;
    }
}
