package com.continuum.vendor.service.model.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
public class ChargingStationDetailsResponse extends ChargingStationResponse {
    private BigDecimal carbonCreditGenerated;
    private List<ChargerDetailsResponse> chargerDetailsResponse;
    private List<EvseDetailsResponse> evseDetailsResponse;
    private List<ConnectorDetailsResponse> connectorDetailsResponse;
}
