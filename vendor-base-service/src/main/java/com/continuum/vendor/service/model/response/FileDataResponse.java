package com.continuum.vendor.service.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDataResponse {
    private UUID id;
    private String name;
    private String type;
    private String identifier;
    private String contentType;
    private String data;
    @JsonProperty("dateCreated")
    private Date dateCreated;
    @JsonProperty("dateUpdated")
    private Date dateUpdated;

}
