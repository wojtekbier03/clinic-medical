package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public DoctorDto addDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.addDoctor(doctorDto);
    }

    @GetMapping
    public List<DoctorDto> getAllDoctors(Pageable pageable){
        return doctorService.getAllDoctors(pageable);
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PatchMapping("/{doctorId}/clinics/{clinicId}")
    public void assignDoctorToClinic(@PathVariable Long doctorId, @PathVariable Long clinicId) {
        doctorService.assignDoctor(doctorId, clinicId);
    }
}