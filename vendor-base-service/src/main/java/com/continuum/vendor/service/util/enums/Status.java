package com.continuum.vendor.service.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    AVAILABLE("available") ,
    ACTIVE("active"),
    INITIATED("Initiated"),
    PROCESSING("Processing"),
    FAILED("Failed");
    private final String name;
}
