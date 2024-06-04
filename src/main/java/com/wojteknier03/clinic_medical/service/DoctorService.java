package com.wojteknier03.clinic_medical.service;

import com.wojteknier03.clinic_medical.dto.DoctorDto;
import com.wojteknier03.clinic_medical.mapper.DoctorMapper;
import com.wojteknier03.clinic_medical.model.Clinic;
import com.wojteknier03.clinic_medical.model.Doctor;
import com.wojteknier03.clinic_medical.repository.ClinicRepository;
import com.wojteknier03.clinic_medical.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorMapper doctorMapper;

    public DoctorDto addDoctor(DoctorDto doctorDto) {
        Doctor doctor = doctorMapper.fromDto(doctorDto);

        Set<Clinic> clinics = doctorDto.getClinicIds().stream()
                .map(clinicId -> clinicRepository.findById(clinicId)
                        .orElseThrow(() -> new IllegalArgumentException("Clinic not found")))
                .collect(Collectors.toSet());

        doctor.setClinics(clinics);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }

    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctorMapper.toDtoList(doctors);
    }

    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return doctorMapper.toDto(doctor);
    }

    public void deleteDoctor(Long id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        Doctor doctor = optionalDoctor.orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        doctorRepository.delete(doctor);
    }

    public void assignDoctor(Long doctorId, Long clinicId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));

        doctor.getClinics().add(clinic);
        doctorRepository.save(doctor);
    }
}