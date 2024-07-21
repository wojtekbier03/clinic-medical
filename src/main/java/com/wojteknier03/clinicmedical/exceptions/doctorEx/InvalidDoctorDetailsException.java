package com.wojteknier03.clinicmedical.exceptions.doctorEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class InvalidDoctorDetailsException extends ClinicMedicalException {

    public InvalidDoctorDetailsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
