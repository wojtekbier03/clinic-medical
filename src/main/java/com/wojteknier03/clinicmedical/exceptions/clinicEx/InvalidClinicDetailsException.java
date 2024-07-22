package com.wojteknier03.clinicmedical.exceptions.clinicEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class InvalidClinicDetailsException extends ClinicMedicalException {
    public InvalidClinicDetailsException() {
        super("Invalid clinic details provided", HttpStatus.BAD_REQUEST);
    }
}
