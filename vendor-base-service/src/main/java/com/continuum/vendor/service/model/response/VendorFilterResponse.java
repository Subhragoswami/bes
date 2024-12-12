package com.continuum.vendor.service.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class VendorFilterResponse {
    private UUID id;
    private UUID userId;
    private UUID vendorId;
    private String type;
    private String status;
    private UUID fileId;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime startDate;
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime endDate;
    private String dateCreated;
}
