package com.continuum.vendor.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class VendorReportDetailsResponse {
    private String stationName;
    private String Location;
    private String userName;
    private String email;
    private String phoneNumber;
    private String vehicleName;
    private String vehicleNumber;
    private UUID id;
    private String customerTransactionId;
    private int certificateNumber;
    private String status;
    private Date date;
    private String category;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private String startAt;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private String stopAt;
    private String usedEnergy;
    private double latitude;
    private double longitude;
    private BigDecimal carbonCreditGenerated;
}
