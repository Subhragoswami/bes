package com.continuum.vendor.service.model.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargingSessionResponse {
    private String connectorId;
    private String startAt;
    private String stopAt;
    private String cdrToken;
    private String name;
    private String locationName;
    private GeoLocation geoLocation;
    private Long locationId;
    private String sessionId;
    private String city;
    private String address;
    private String vehicleName;
    private String vehicleNumber;
    private String vehicleType;
    private String usedEnergy;
    private String currency;
    private String authorizationReference;
    private BigDecimal carbonCreditGenerated;
    private UserDetails userDetails;
}
