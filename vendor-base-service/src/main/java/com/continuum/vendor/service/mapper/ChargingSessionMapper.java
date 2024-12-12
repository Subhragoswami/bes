package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.cms.ChargingSessionCMS;
import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.entity.vendor.VendorCustomer;
import com.continuum.vendor.service.model.response.ChargingSessionResponse;
import com.continuum.vendor.service.model.response.GeoLocation;
import com.continuum.vendor.service.model.response.UserDetails;
import com.continuum.vendor.service.util.DateUtil;
import com.continuum.vendor.service.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.List;

@Component
@Slf4j
public class ChargingSessionMapper {
    private final MapperFactory mapperFactory;
    private final MapperFacade mapperFacade;

    @Autowired
    public ChargingSessionMapper() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFacade = mapperFactory.getMapperFacade();
        configureMapper();
    }

    private void configureMapper() {
        mapperFactory.classMap(ChargingSession.class, ChargingSessionResponse.class)
                .field("chargingStation.locationId", "locationId")
                .field("transactionId", "sessionId")
                .field("chargingStation.name", "name")
                .field("chargingStation.city", "city")
                .field("latitude", "geoLocation.latitude")
                .field("longitude", "geoLocation.longitude")
                .field("vendorCustomer.firstName", "userDetails.firstName")
                .field("vendorCustomer.lastName", "userDetails.lastName")
                .field("vendorCustomer.userType", "userDetails.userType")
                .field("carbonCredits.creditScorePoints", "carbonCreditGenerated")
                .customize(new CustomMapper<ChargingSession, ChargingSessionResponse>() {
                    @Override
                    public void mapAtoB(ChargingSession source, ChargingSessionResponse target, MappingContext context) {
                        if (source.getVendorCustomer().getEmail() != null) {
                            target.getUserDetails().setEmail(EncryptionUtil.decryptEmailMobile(source.getVendorCustomer().getEmail()));
                        }
                        if (source.getVendorCustomer().getMobile() != null) {
                            target.getUserDetails().setMobile(EncryptionUtil.decryptEmailMobile(source.getVendorCustomer().getMobile()));
                        }
                        if(ObjectUtils.isNotEmpty(source.getStopAt()) && ObjectUtils.isNotEmpty(source.getStopAt())){
                            target.setStartAt(DateUtil.getISTToUTC(source.getStartAt()));
                            target.setStopAt(DateUtil.getISTToUTC(source.getStopAt()));
                        }
                    }
                })
                .byDefault()
                .mapNulls(false)
                .register();


        mapperFactory.classMap(ChargingSession.class, ChargingSessionCMS.class)
                .byDefault()
                .mapNulls(false)
                .register();
    }


    public List<ChargingSessionResponse> mapToChargingSessions(List<ChargingSession> transactions) {
        log.info("Mapping charging session data : {}", transactions);
        return transactions.stream()
                .map(this::mapTransactionToChargingSession)
                .collect(Collectors.toList());
    }

    private ChargingSessionResponse mapTransactionToChargingSession(ChargingSession chargingSession) {
        ChargingSessionResponse chargingSessionResponse = mapperFacade.map(chargingSession, ChargingSessionResponse.class);
        chargingSessionResponse.setCarbonCreditGenerated(BigDecimal.ZERO);
        chargingSessionResponse.setLocationId(chargingSession.getChargingStation().getLocationId());
        chargingSessionResponse.setName(chargingSession.getChargingStation().getName());
        chargingSessionResponse.setCity(chargingSession.getChargingStation().getCity());
        //chargingSessionResponse.setStationType(chargingSession.getStationType());
        GeoLocation geoLocation = setGeoLocation(chargingSession);
        chargingSessionResponse.setGeoLocation(geoLocation);
        UserDetails userDetails = setUserDetails(chargingSession.getVendorCustomer());
        chargingSessionResponse.setUserDetails(userDetails);
        return chargingSessionResponse;
    }

    private UserDetails setUserDetails(VendorCustomer vendorCustomer) {
        return UserDetails.builder()
                .email(vendorCustomer.getEmail())
                .firstName(vendorCustomer.getFirstName())
                .lastName(vendorCustomer.getLastName())
                .mobile(vendorCustomer.getMobile())
                .userType(vendorCustomer.getUserType())
                .build();
    }
    private GeoLocation setGeoLocation(ChargingSession chargingSession){
        return GeoLocation.builder()
                .latitude(chargingSession.getLatitude())
                .longitude(chargingSession.getLongitude())
                .build();
    }

    public List<ChargingSessionResponse> mapToChargingSessionList(List<ChargingSession> chargingSessionList) {
        log.info("Mapping Charging-session list.");
        return mapperFacade.mapAsList(chargingSessionList, ChargingSessionResponse.class);
    }

    public List<ChargingSessionCMS> mapToChargingSessionListCMS(List<ChargingSession> chargingSessionList, String vendorCode) {
        log.info("Mapping Charging-session list.");
        List<ChargingSessionCMS> chargingSessionCMSList = mapperFacade.mapAsList(chargingSessionList, ChargingSessionCMS.class);
        chargingSessionCMSList.forEach(chargingSessionCMS -> {
            chargingSessionCMS.setVendorCode(vendorCode);
        });
        return chargingSessionCMSList;
    }
}
