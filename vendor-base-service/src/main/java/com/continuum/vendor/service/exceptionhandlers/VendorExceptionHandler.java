package com.continuum.vendor.service.exceptionhandlers;

import com.continuum.vendor.service.exceptions.VendorException;
import com.continuum.vendor.service.exceptions.VendorSecurityException;
import com.continuum.vendor.service.exceptions.ValidationException;
import com.continuum.vendor.service.model.response.ErrorDto;
import com.continuum.vendor.service.model.response.ResponseDto;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class VendorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VendorSecurityException.class)
    public ResponseEntity<Object> handleSecurityException(VendorSecurityException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(VendorException.class)
    public ResponseEntity<Object> handleContinuumException(VendorException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        if(CollectionUtils.isEmpty(ex.getErrorMessages())) {
            ErrorDto errorDto = ErrorDto.builder()
                    .errorCode(ex.getErrorCode())
                    .errorMessage(ex.getErrorMessage())
                    .build();
            return generateResponseWithErrors(List.of(errorDto));
        }
        return generateResponseWithErrors(ex.getErrorMessages());
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Error in handleGenericException ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(
                ResponseDto.builder()
                        .status(1)
                        .errors(List.of(errorDto))
                        .build());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        logger.error("Error in handleConflict ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.CONFLICT.value()))
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ResponseDto.builder()
                        .status(1)
                        .errors(List.of(errorDto))
                        .build());
    }
    private ResponseEntity<Object> generateResponseWithErrors(List<ErrorDto> errors) {
        return ResponseEntity.ok().body(
                ResponseDto.builder()
                        .status(1)
                        .errors(errors)
                        .build());
    }
}
