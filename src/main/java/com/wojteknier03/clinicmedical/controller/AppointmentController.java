package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public List<AppointmentDto> getAppointments(Pageable pageable){
        return appointmentService.getAppointments(pageable);
    }

    @PatchMapping("{appointmentId}/patients/{patientId}")
    public void assignPatientToAppointment(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        appointmentService.assignPatientToAppointment(appointmentId, patientId);
    }
}
