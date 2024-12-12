package com.continum.vendor.service.service;
import com.continuum.vendor.service.dao.ChargingSessionDao;
import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.model.request.ChargingSessionFilter;
import com.continuum.vendor.service.model.response.ChargingSessionResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.service.ChargingSessionService;
import com.continuum.vendor.service.mapper.ChargingSessionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.List;

import static com.continuum.vendor.service.util.AppConstants.RESPONSE_SUCCESS;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ChargingSessionServiceTest {
    @Mock
    private ChargingSessionDao chargingSessionDao;

    @Mock
    private ChargingSessionMapper sessionMapper;

    @Test
    void testGetAllCustomerDetails() {
        ChargingSessionFilter chargingSessionFilter = new ChargingSessionFilter();
        String search = "test";
        Pageable pageable = Pageable.ofSize(10);

        List<ChargingSession> sessionsList = Collections.singletonList(new ChargingSession());
        Page<ChargingSession> page = new PageImpl<>(sessionsList);
        when(chargingSessionDao.getAllChargingSession(chargingSessionFilter, search, pageable))
                .thenReturn(page);

        List<ChargingSessionResponse> sessionResponses = Collections.singletonList(new ChargingSessionResponse());
        when(sessionMapper.mapToChargingSessionList(sessionsList)).thenReturn(sessionResponses);

        ChargingSessionService chargingSessionService = new ChargingSessionService(chargingSessionDao, sessionMapper);
        ResponseDto<ChargingSessionResponse> response = chargingSessionService.getChargingSessionDetails(chargingSessionFilter, search, pageable);

        verify(chargingSessionDao).getAllChargingSession(chargingSessionFilter, search, pageable);
        verify(sessionMapper).mapToChargingSessionList(sessionsList);
        assertEquals(sessionResponses, response.getData());
        assertEquals(RESPONSE_SUCCESS, response.getStatus());
        assertEquals(1, response.getTotal());
    }

    @Test
    void testGetAllCustomerDetailsEmptyList() {
        ChargingSessionFilter chargingSessionFilter = new ChargingSessionFilter();
        String search = "test";
        Pageable pageable = Pageable.ofSize(10);

        Page<ChargingSession> page = new PageImpl<>(Collections.emptyList());
        when(chargingSessionDao.getAllChargingSession(chargingSessionFilter, search, pageable))
                .thenReturn(page);
        ChargingSessionService chargingSessionService = new ChargingSessionService(chargingSessionDao, sessionMapper);
        ResponseDto<ChargingSessionResponse> response = chargingSessionService.getChargingSessionDetails(chargingSessionFilter, search, pageable);

        assertTrue(response.getData().isEmpty());
        assertEquals(RESPONSE_SUCCESS, response.getStatus());
        assertEquals(0, response.getTotal());
    }

    @Test
    void testGetAllCustomerDetailsNullFilter() {
        String search = "test";
        Pageable pageable = Pageable.ofSize(10);

        Page<ChargingSession> page = new PageImpl<>(Collections.emptyList());
        when(chargingSessionDao.getAllChargingSession(null, search, pageable))
                .thenReturn(page);

        ChargingSessionService chargingSessionService = new ChargingSessionService(chargingSessionDao, sessionMapper);
        ResponseDto<ChargingSessionResponse> response = chargingSessionService.getChargingSessionDetails(null, search, pageable);

        assertTrue(response.getData().isEmpty());
        assertEquals(RESPONSE_SUCCESS, response.getStatus());
        assertEquals(0, response.getTotal());
    }

    @Test
    void testGetAllCustomerDetailsExceptionHandlingNullList() {
        ChargingSessionFilter chargingSessionFilter = new ChargingSessionFilter();
        String search = "test";
        Pageable pageable = Pageable.ofSize(10);

        when(chargingSessionDao.getAllChargingSession(chargingSessionFilter, search, pageable))
                .thenReturn(null);

        ChargingSessionService chargingSessionService = new ChargingSessionService(chargingSessionDao, sessionMapper);
        assertThrows(NullPointerException.class, () -> chargingSessionService.getChargingSessionDetails(chargingSessionFilter, search, pageable));
    }
}
