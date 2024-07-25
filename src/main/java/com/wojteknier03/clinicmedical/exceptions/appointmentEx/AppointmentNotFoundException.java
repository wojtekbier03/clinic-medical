package com.wojteknier03.clinicmedical.exceptions.appointmentEx;

import com.wojteknier03.clinicmedical.exceptions.ClinicMedicalException;
import org.springframework.http.HttpStatus;

public class AppointmentNotFoundException extends ClinicMedicalException {

    public AppointmentNotFoundException(String message) {
        super("Appointment not found", HttpStatus.NOT_FOUND);

    }
}
