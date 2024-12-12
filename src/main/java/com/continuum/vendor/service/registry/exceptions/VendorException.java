package com.continuum.vendor.service.registry.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class VendorException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

}

