package com.wojteknier03.clinicmedical.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPaginationParametersException extends ClinicMedicalException {
    public InvalidPaginationParametersException() {
        super("Invalid pagination parameters", HttpStatus.BAD_REQUEST);
    }
}
