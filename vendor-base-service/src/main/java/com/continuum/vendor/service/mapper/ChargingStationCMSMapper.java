package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.cms.ChargerDetailsCMS;
import com.continuum.vendor.service.entity.cms.ChargingStationCMS;
import com.continuum.vendor.service.entity.cms.ConnectorDetailsCMS;
import com.continuum.vendor.service.entity.cms.EVSEDetailsCMS;
import com.continuum.vendor.service.entity.vendor.ChargerDetails;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.entity.vendor.ConnectorDetails;
import com.continuum.vendor.service.entity.vendor.EVSEDetails;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChargingStationCMSMapper {
    private final MapperFactory mapperFactory;
    private final MapperFacade mapperFacade;

    @Autowired
    public ChargingStationCMSMapper() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFacade = mapperFactory.getMapperFacade();
        configureMapper();
    }

    private void configureMapper() {

        mapperFactory.classMap(ChargingStation.class, ChargingStationCMS.class)
                .byDefault()
                .mapNulls(false)
                .register();

        mapperFactory.classMap(ChargerDetails.class, ChargerDetailsCMS.class)
                .byDefault()
                .mapNulls(false)
                .register();

        mapperFactory.classMap(EVSEDetails.class, EVSEDetailsCMS.class)
                .byDefault()
                .mapNulls(false)
                .register();

        mapperFactory.classMap(ConnectorDetails.class, ConnectorDetailsCMS.class)
                .byDefault()
                .mapNulls(false)
                .register();
    }


    public List<ChargingStationCMS> mapToChargingStationListCMS(List<ChargingStation> chargingStationList, String vendorCode) {
        List<ChargingStationCMS> chargingStationCMSList = mapperFacade.mapAsList(chargingStationList, ChargingStationCMS.class);
        chargingStationCMSList.forEach(data -> {
            data.setVendorCode(vendorCode);
        });
        return chargingStationCMSList;
    }

    public List<ChargerDetailsCMS> mapToChargerListCMS(List<ChargerDetails> chargerDetailsList, String vendorCode) {
        List<ChargerDetailsCMS> chargerDetailsCMSList = mapperFacade.mapAsList(chargerDetailsList, ChargerDetailsCMS.class);
        chargerDetailsCMSList.forEach(data -> {
            data.setVendorCode(vendorCode);
        });
        return chargerDetailsCMSList;
    }

    public List<EVSEDetailsCMS> mapToEVSEListCMS(List<EVSEDetails> evseDetailsList, String vendorCode) {
        List<EVSEDetailsCMS> evseDetailsCMSList = mapperFacade.mapAsList(evseDetailsList, EVSEDetailsCMS.class);
        evseDetailsCMSList.forEach(data -> {
            data.setVendorCode(vendorCode);
        });
        return evseDetailsCMSList;
    }

    public List<ConnectorDetailsCMS> mapToConnectorListCMS(List<ConnectorDetails> connectorDetailsList, String vendorCode) {
        List<ConnectorDetailsCMS> connectorDetailsCMSList = mapperFacade.mapAsList(connectorDetailsList, ConnectorDetailsCMS.class);
        connectorDetailsCMSList.forEach(data -> {
            data.setVendorCode(vendorCode);
        });
        return connectorDetailsCMSList;
    }
}
