package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.vendor.ChargerDetails;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.entity.vendor.ConnectorDetails;
import com.continuum.vendor.service.entity.vendor.EVSEDetails;
import com.continuum.vendor.service.model.response.*;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChargingStationMapper {

    private final MapperFactory mapperFactory;
    public ChargingStationMapper() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        configureMapper();
    }

    private void configureMapper() {
        mapperFactory.classMap(ChargingStation.class, ChargingStationResponse.class)
                .field("latitude", "coordinates.latitude")
                .field("longitude", "coordinates.longitude")
                .byDefault()
                .register();
        mapperFactory.classMap(ChargingStation.class, ChargingStationDetailsResponse.class)
                .field("latitude", "coordinates.latitude")
                .field("longitude", "coordinates.longitude")
                .byDefault()
                .register();
        mapperFactory.classMap(ChargerDetails.class, ChargerDetailsResponse.class)
                .byDefault()
                .register();
        mapperFactory.classMap(EVSEDetails.class, EvseDetailsResponse.class)
                .byDefault()
                .register();
        mapperFactory.classMap(ConnectorDetails.class, ConnectorDetailsResponse.class)
                .byDefault()
                .register();
    }

    public <T> List<T> mapChargingStationToResponse(Page<?> sourceList, Class<T> targetType) {
        return sourceList.stream()
                .map(source -> mapperFactory.getMapperFacade().map(source, targetType))
                .collect(Collectors.toList());
    }
    public ChargingStationDetailsResponse mapToChargingStationToResponse(ChargingStation chargingStation, List<ChargerDetails> chargerDetails, List<EVSEDetails> evseDetails, List<ConnectorDetails> connectorDetails) {
        ChargingStationDetailsResponse chargingStationResponse = mapperFactory.getMapperFacade().map(chargingStation, ChargingStationDetailsResponse.class);
        chargingStationResponse.setChargerDetailsResponse(mapChargerDetails(chargerDetails));
        chargingStationResponse.setEvseDetailsResponse(mapEvseDetails(evseDetails));
        chargingStationResponse.setConnectorDetailsResponse(mapConnectorDetails(connectorDetails));
        return chargingStationResponse;
    }
    private List<ChargerDetailsResponse> mapChargerDetails(List<ChargerDetails> chargerDetailsList) {
        return chargerDetailsList.stream()
                .map(chargerDetails -> mapperFactory.getMapperFacade().map(chargerDetails, ChargerDetailsResponse.class))
                .collect(Collectors.toList());
    }
    private List<EvseDetailsResponse> mapEvseDetails(List<EVSEDetails> evseDetailsList) {
        return evseDetailsList.stream()
                .map(evseDetails -> mapperFactory.getMapperFacade().map(evseDetails, EvseDetailsResponse.class))
                .collect(Collectors.toList());
    }
    private List<ConnectorDetailsResponse> mapConnectorDetails(List<ConnectorDetails> connectorDetailsList) {
        return connectorDetailsList.stream()
                .map(connectorDetails -> mapperFactory.getMapperFacade().map(connectorDetails, ConnectorDetailsResponse.class))
                .collect(Collectors.toList());
    }
}
