package com.wojteknier03.clinicmedical.exceptions.patientEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends ClinicMedicalException {

    public PatientNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}