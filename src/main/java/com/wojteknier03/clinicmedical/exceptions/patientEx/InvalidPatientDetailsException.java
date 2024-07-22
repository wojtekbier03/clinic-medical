package com.wojteknier03.clinicmedical.exceptions.patientEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class InvalidPatientDetailsException extends ClinicMedicalException {

    public InvalidPatientDetailsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}