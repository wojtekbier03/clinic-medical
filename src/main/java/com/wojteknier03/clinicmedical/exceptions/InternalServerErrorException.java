package com.wojteknier03.clinicmedical.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ClinicMedicalException {
    public InternalServerErrorException() {
        super("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
