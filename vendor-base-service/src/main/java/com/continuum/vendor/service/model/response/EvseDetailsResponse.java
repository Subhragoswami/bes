package com.continuum.vendor.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class EvseDetailsResponse {
    private UUID id;

    private String physicalReference;
    private String uid;
    private String maxOutputPower;
    private String status;
    private int connectorId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String connectorStatus;
    private String availability;
}
