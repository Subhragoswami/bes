package com.continuum.vendor.service.model.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@Data
@SuperBuilder
@NoArgsConstructor
public class ChargingStationResponse {
    private UUID id;
    private long locationId;
    private String name;
    private Coordinates coordinates;
    private String parkingType;
    private String email;
    private String phone;
    private String image;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private String status;
}
