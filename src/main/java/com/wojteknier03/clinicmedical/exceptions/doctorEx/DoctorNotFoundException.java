package com.wojteknier03.clinicmedical.exceptions.doctorEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends ClinicMedicalException {

    public DoctorNotFoundException(String message) {
        super("Doctor not found", HttpStatus.NOT_FOUND);
    }
}