package com.wojteknier03.clinicmedical.exceptions.appointmentEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppointmentGlobalExceptionHandler {

    @ExceptionHandler(ClinicMedicalException.class)
    public ResponseEntity<Object> handleClinicMedicalException(ClinicMedicalException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

