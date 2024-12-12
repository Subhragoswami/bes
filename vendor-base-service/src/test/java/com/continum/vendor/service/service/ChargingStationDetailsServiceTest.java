package com.continum.vendor.service.service;

import com.continuum.vendor.service.dao.ChargingStationDao;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.exceptions.ValidationException;
import com.continuum.vendor.service.mapper.ChargingStationMapper;
import com.continuum.vendor.service.model.response.ChargingStationDetailsResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.service.ChargingStationService;
import com.continuum.vendor.service.util.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChargingStationDetailsServiceTest {
    @Mock
    private ChargingStationDao chargingStationDao;
    @Mock
    private ChargingStationMapper chargingStationMapper;
    @InjectMocks
    private ChargingStationService chargingStationService;
    private ChargingStation chargingStation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        chargingStation = new ChargingStation();
        chargingStation.setLocationId(1L);
    }
    @Test
    void testGetChargingStationDetails_success() {
        when(chargingStationDao.findByLocatonId(anyLong())).thenReturn(Optional.of(chargingStation));
        ChargingStationDetailsResponse response = new ChargingStationDetailsResponse();
        when(chargingStationMapper.mapToChargingStationToResponse(any(), any(), any(), any())).thenReturn(response);

        ResponseDto<ChargingStationDetailsResponse> result = chargingStationService.getChargingStationDetails(1L);

        assertNotNull(result);
        assertEquals(AppConstants.RESPONSE_SUCCESS, result.getStatus());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testGetChargingStationDetails_notFound() {
        when(chargingStationDao.findByLocatonId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> {
            chargingStationService.getChargingStationDetails(1L);
        });
    }

    @Test
    void testGetCities() {
        List<String> mockCities = Arrays.asList("City1", "City2", "City3");
        when(chargingStationDao.getCityList()).thenReturn(mockCities);
        ResponseDto<String> responseDto = chargingStationService.getCityList();
        assertEquals(mockCities, responseDto.getData());
    }
    @Test
    void testGetCitiesEmptyList() {
        when(chargingStationDao.getCityList()).thenReturn(Collections.emptyList());
        ResponseDto<String> responseDto = chargingStationService.getCityList();
        assertEquals(Collections.emptyList(), responseDto.getData());
    }

}
