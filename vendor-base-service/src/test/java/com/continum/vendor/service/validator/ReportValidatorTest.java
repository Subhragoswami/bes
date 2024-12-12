package com.continum.vendor.service.validator;

import com.continuum.vendor.service.exceptions.ValidationException;
import com.continuum.vendor.service.util.ErrorConstants;
import com.continuum.vendor.service.validator.ReportValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReportValidatorTest {
    @Test
    public void testReportRequestDateValidation_validDates() {
        Date startDate = new Date(1672531200000L);
        Date endDate = new Date(1704067199000L);

        assertDoesNotThrow(() -> ReportValidator.reportRequestDateValidation(startDate, endDate));
    }


    @Test
    public void testReportRequestDateValidation_invalidDates() {
        Date startDate = new Date(1704067199000L);
        Date endDate = new Date(1672531200000L);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                ReportValidator.reportRequestDateValidation(startDate, endDate));
        assertTrue(exception.getErrorMessage().contains("start date is Invalid."));
    }

    @Test
    public void testReportRequestIdAndDateValidation_validInputs() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59, 59);
        UUID userId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();

        assertDoesNotThrow(() -> ReportValidator.reportRequestIdAndDateValidation(startDate, endDate, userId, vendorId));
    }

    @Test
    public void testReportRequestIdAndDateValidation_invalidDates() {
        LocalDateTime startDate = LocalDateTime.of(2023, 12, 31, 23, 59, 59);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        UUID userId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();

        ValidationException exception = assertThrows(ValidationException.class, () ->
                ReportValidator.reportRequestIdAndDateValidation(startDate, endDate, userId, vendorId));
        assertTrue(exception.getErrorMessage().contains("start date is Invalid."));
    }


    @Test
    public void testReportRequestIdAndDateValidation_missingUserIdAndVendorId() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                ReportValidator.reportRequestIdAndDateValidation(startDate, endDate, null, null));
        assertTrue(exception.getErrorMessage().contains("userId & vendorId is mandatory"));
    }
}
