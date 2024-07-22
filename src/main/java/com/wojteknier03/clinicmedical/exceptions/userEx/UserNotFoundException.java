package com.wojteknier03.clinicmedical.exceptions.userEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClinicMedicalException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

