package com.wojteknier03.clinic_medical.controller;

import com.wojteknier03.clinic_medical.dto.ClinicDto;
import com.wojteknier03.clinic_medical.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping
    public ClinicDto addClinic(@RequestBody ClinicDto clinicDto) {
        return clinicService.addClinic(clinicDto);
    }

    @GetMapping
    public List<ClinicDto> getAllClinics() {
        return clinicService.getClinics();
    }

    @GetMapping("/{id}")
    public ClinicDto getClinicById(@PathVariable Long id){
        return clinicService.getClinicById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteClinic(@PathVariable Long id){
        clinicService.deleteClinic(id);
    }
}
