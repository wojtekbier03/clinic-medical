package com.wojteknier03.clinicmedical.exceptions.appointmentEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class InvalidAppointmentDetailsException extends ClinicMedicalException {
    public InvalidAppointmentDetailsException() {
        super("Invalid appointment details provided", HttpStatus.BAD_REQUEST);
    }
}

