package com.continuum.vendor.service.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataCategory {
    CHARGING_STATION("CHARGING_STATION"),
    CHARGING_SESSION("CHARGING_SESSION");

    public final String name;
}
