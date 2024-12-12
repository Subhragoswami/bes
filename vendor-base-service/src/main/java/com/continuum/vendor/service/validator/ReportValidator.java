package com.continuum.vendor.service.validator;

import com.continuum.vendor.service.exceptions.ValidationException;
import com.continuum.vendor.service.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportValidator {
    public static void reportRequestDateValidation(Date startDate, Date endDate) {
        if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate) && endDate.before(startDate)) {
            throw new ValidationException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_CODE_MESSAGE,"start date"));
        }
    }
    public static void reportRequestIdAndDateValidation(LocalDateTime startDate, LocalDateTime endDate, UUID userId, UUID vendorId){
        if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate) && endDate.isBefore(startDate)) {
            throw new ValidationException(ErrorConstants.INVALID_ERROR_CODE,  MessageFormat.format(ErrorConstants.INVALID_ERROR_CODE_MESSAGE,"start date"));
        }
        if(ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(vendorId)){
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "userId & vendorId"));
        }

    }
}
