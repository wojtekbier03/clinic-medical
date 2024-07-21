package com.wojteknier03.clinicmedical.exceptions.userEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class InvalidUserDetailsException extends ClinicMedicalException {

    public InvalidUserDetailsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
