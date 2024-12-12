package com.continuum.vendor.service.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleType {
    TWO_WHEELER("2 WHEELER"),
    THREE_WHEELER("3 WHEELER"),
    FOUR_WHEELER("4 WHEELER"),
    HEAVY("HEAVY");

    private final String name;

    public static VehicleType fromString(String text) {
        for (VehicleType type : VehicleType.values()) {
            if (type.name.equals(text)) {
                return type;
            }
        }
        return null;
    }
}
