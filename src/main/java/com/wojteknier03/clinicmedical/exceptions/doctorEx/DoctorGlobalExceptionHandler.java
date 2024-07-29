package com.wojteknier03.clinicmedical.exceptions.doctorEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import com.wojteknier03.clinicmedical.exceptions.patientEx.PatientNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class DoctorGlobalExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleDoctorNotFoundException(PatientNotFoundException ex, WebRequest request) {
        return handleGlobalException(ex, request);
    }

    @ExceptionHandler(ClinicMedicalException.class)
    public ResponseEntity<Object> handleClinicMedicalException(
            ClinicMedicalException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
