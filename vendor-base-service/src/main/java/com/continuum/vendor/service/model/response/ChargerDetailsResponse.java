package com.continuum.vendor.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ChargerDetailsResponse {
    private UUID id;
    private UUID chargingDetailsId;
    private String identity;
    private String chargerName;
    private String chargePointOem;
    private String chargePointDevice;
    private String tariff;
    private String chargePointConnectionProtocol;
    private String floorLevel;
    private String qrCode;
    private String chargerId;
    private String stationType;

}
