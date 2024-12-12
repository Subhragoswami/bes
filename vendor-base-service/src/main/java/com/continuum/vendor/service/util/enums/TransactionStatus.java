package com.continuum.vendor.service.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatus {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    PENDING("PENDING"),
    IN_PROGRESS("IN-PROGRESS");

    public final String name;
}
