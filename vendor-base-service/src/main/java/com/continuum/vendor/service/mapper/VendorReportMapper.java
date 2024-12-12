package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.vendor.CarbonCredits;
import com.continuum.vendor.service.entity.vendor.ChargingSession;
import com.continuum.vendor.service.entity.vendor.VendorReport;
import com.continuum.vendor.service.model.response.VendorFilterResponse;
import com.continuum.vendor.service.model.response.VendorReportDetailsResponse;
import com.continuum.vendor.service.util.DateUtil;
import com.continuum.vendor.service.util.enums.Category;
import com.continuum.vendor.service.util.enums.Status;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class VendorReportMapper {

    private final MapperFactory mapperFactory;
    private final MapperFacade mapperFacade;

    public VendorReportMapper() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFacade = mapperFactory.getMapperFacade();
        configureMapper();
    }

    private void configureMapper() {
        mapperFactory.classMap(ChargingSession.class, VendorReportDetailsResponse.class)
                .field("chargingStation.name", "stationName")
                .field("vendorCustomer.email", "email")
                .field("vendorCustomer.mobile", "phoneNumber")
                .customize(new CustomMapper<ChargingSession, VendorReportDetailsResponse>() {
                    @Override
                    public void mapAtoB(ChargingSession chargingSession, VendorReportDetailsResponse response, MappingContext context) {
                        if (ObjectUtils.isNotEmpty(chargingSession.getVendorCustomer()) && ObjectUtils.isNotEmpty(chargingSession.getVendorCustomer().getFirstName()) && ObjectUtils.isNotEmpty(chargingSession.getVendorCustomer().getLastName())) {
                            response.setUserName(chargingSession.getVendorCustomer().getFirstName() + " " + chargingSession.getVendorCustomer().getLastName());
                        }
                        else{
                            response.setUserName("");
                        }

                        if (ObjectUtils.isNotEmpty(chargingSession.getChargingStation())) {
                            String location = chargingSession.getAddress();
                            location += (ObjectUtils.isNotEmpty(chargingSession.getChargingStation().getCity())) ? ", " + chargingSession.getChargingStation().getCity() : "";
                            location += (ObjectUtils.isNotEmpty(chargingSession.getChargingStation().getState())) ? ", " + chargingSession.getChargingStation().getState() : "";
                            response.setLocation(location);
                            response.setStationName(chargingSession.getChargingStation().getName());
                        } else {
                            response.setLocation(chargingSession.getAddress());
                        }

                        response.setStatus(Status.ACTIVE.getName());
                        response.setCategory(Category.CARBON_CREDIT.getName());
                    }
                })
                .byDefault()
                .mapNulls(false)
                .register();

        mapperFactory.classMap(CarbonCredits.class, VendorReportDetailsResponse.class)
                .field("dateCreated", "date")
                .field("transactionId", "customerTransactionId")
                .byDefault()
                .mapNulls(false)
                .register();

        mapperFactory.classMap(VendorReport.class, VendorFilterResponse.class)
                .customize(new CustomMapper<VendorReport, VendorFilterResponse>() {
                    @Override
                    public void mapAtoB(VendorReport vendorReport, VendorFilterResponse response, MappingContext context) {
                        response.setDateCreated(DateUtil.formatDateToString(vendorReport.getDateCreated()));
                    }
                })

                .byDefault()
                .mapNulls(false)
                .register();
    }

//    public VendorReportDetailsResponse mapToVendorReportDetailsResponse(ChargingSession chargingSession, CarbonCredits carbonCredits, Map<String, List<String>> decryptedEmails, Map<String, List<String>> decryptedPhones) {
//        VendorReportDetailsResponse response = new VendorReportDetailsResponse();
//        if (ObjectUtils.isNotEmpty(chargingSession)) {
//            response = mapperFacade.map(chargingSession, VendorReportDetailsResponse.class);
//            if (ObjectUtils.isNotEmpty(chargingSession.getVendorCustomer())) {
//                List<String> decryptedEmailList = decryptedEmails.get(chargingSession.getVendorCustomer().getEmail());
//                List<String> decryptedPhoneList = decryptedPhones.get(chargingSession.getVendorCustomer().getMobile());
//
//                if (decryptedEmailList != null && !decryptedEmailList.isEmpty()) {
//                    response.setEmail(decryptedEmailList.get(0));
//                }
//                if (decryptedPhoneList != null && !decryptedPhoneList.isEmpty()) {
//                    response.setPhoneNumber(decryptedPhoneList.get(0));
//                }
//            }
//        }
//        if (ObjectUtils.isNotEmpty(carbonCredits)) {
//            mapperFacade.map(carbonCredits, response);
//        }
//        return response;
//    }

    public VendorReportDetailsResponse mapToVendorReportDetailsResponse(ChargingSession chargingSession, CarbonCredits carbonCredits, Map<String, List<String>> decryptedEmails, Map<String, List<String>> decryptedPhones) {
        VendorReportDetailsResponse response = new VendorReportDetailsResponse();

        if (ObjectUtils.isNotEmpty(chargingSession)) {
            response = mapperFacade.map(chargingSession, VendorReportDetailsResponse.class);

            if (ObjectUtils.isNotEmpty(chargingSession.getVendorCustomer())) {
                if (chargingSession.getVendorCustomer().getEmail() != null) {
                    List<String> decryptedEmailList = decryptedEmails.get(chargingSession.getVendorCustomer().getEmail());
                    if (decryptedEmailList != null && !decryptedEmailList.isEmpty()) {
                        response.setEmail(decryptedEmailList.get(0));
                    }
                }

                if (chargingSession.getVendorCustomer().getMobile() != null) {
                    List<String> decryptedPhoneList = decryptedPhones.get(chargingSession.getVendorCustomer().getMobile());
                    if (decryptedPhoneList != null && !decryptedPhoneList.isEmpty()) {
                        response.setPhoneNumber(decryptedPhoneList.get(0));
                    }
                }
            }
        }

        if (ObjectUtils.isNotEmpty(carbonCredits)) {
            mapperFacade.map(carbonCredits, response);
        }

        return response;
    }


    public VendorFilterResponse mapToVendorReport(VendorReport vendorReport) {
        return mapperFacade.map(vendorReport, VendorFilterResponse.class);
    }
}
