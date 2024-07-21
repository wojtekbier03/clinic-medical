package com.wojteknier03.clinicmedical.exceptions.clinicEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClinicGlobalExceptionHandler {

    @ExceptionHandler(ClinicMedicalException.class)
    public ResponseEntity<String> handleClinicMedicalException(ClinicMedicalException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

