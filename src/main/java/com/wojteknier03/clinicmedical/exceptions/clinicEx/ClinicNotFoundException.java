package com.wojteknier03.clinicmedical.exceptions.clinicEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class ClinicNotFoundException extends ClinicMedicalException {
    public ClinicNotFoundException() {
        super("Clinic not found", HttpStatus.NOT_FOUND);
    }
}
