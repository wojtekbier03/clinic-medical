package com.wojteknier03.clinic_medical.controller;

import com.wojteknier03.clinic_medical.dto.AppointmentDto;
import com.wojteknier03.clinic_medical.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public AppointmentDto addAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.addAppointment(appointmentDto);
    }

    @PatchMapping("{appointmentId}/patients/{patientId}")
    public void assignPatientToAppointment(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        appointmentService.assignPatientToAppointment(appointmentId, patientId);
    }
}