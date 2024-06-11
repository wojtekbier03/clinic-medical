package com.wojteknier03.clinic_medical.controller;

import com.wojteknier03.clinic_medical.dto.AppointmentDto;
import com.wojteknier03.clinic_medical.dto.PatientDto;
import com.wojteknier03.clinic_medical.service.AppointmentService;
import com.wojteknier03.clinic_medical.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;


    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    public PatientDto add(@RequestBody PatientDto patientDto) {
        return patientService.add(patientDto);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email) {
        patientService.delete(email);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatientByEmail(@PathVariable String email, @RequestBody PatientDto updatedPatientDto) {
        return patientService.update(email, updatedPatientDto);
    }

    @GetMapping("/{patientId}/appointments")
    public List<AppointmentDto> getAppointmentsByPatientId(@PathVariable Long patientId) {
        System.out.println("Fetching appointments for patientId: " + patientId); // Debug log
        return appointmentService.getAppointmentByPatientId(patientId);
    }
}