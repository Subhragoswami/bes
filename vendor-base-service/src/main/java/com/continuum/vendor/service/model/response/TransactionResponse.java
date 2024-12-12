package com.continuum.vendor.service.model.response;

import lombok.*;

import java.util.UUID;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private UUID id;
    private String startDate;
    private String endDate;
    private String status;
    private String dataCategory;
    private String description;
    private String createdAt;
    private String updatedAt;
}
