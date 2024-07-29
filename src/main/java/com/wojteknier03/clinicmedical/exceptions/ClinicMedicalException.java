package com.wojteknier03.clinicmedical.exceptions;

import org.springframework.http.HttpStatus;

public class ClinicMedicalException extends RuntimeException{
    private final HttpStatus httpStatus;

    public ClinicMedicalException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
