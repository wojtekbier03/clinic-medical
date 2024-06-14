package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.mapper.DoctorMapper;
import com.wojteknier03.clinicmedical.model.Clinic;
import com.wojteknier03.clinicmedical.model.Doctor;
import com.wojteknier03.clinicmedical.repository.ClinicRepository;
import com.wojteknier03.clinicmedical.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    @Transactional
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

    // 1. Pobranie listy lekarzy (doctorRepository.findAll(pageable).getContent).
    // 2. Zwracamy to, co zwróci metoda toDtoList z doctorMapper.
    public List<DoctorDto> getAllDoctors(Pageable pageable) {
        List<Doctor> doctors = doctorRepository.findAll(pageable).getContent();
        return doctorMapper.toDtoList(doctors);
    }

    // 1. Kiedy lekarz o podanym identyfikatorze nie istnieje (doctorRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek.
    // 2. Kiedy lekarz istnieje (doctorRepository.findById zwraca optional zawierający lekarza), zwracamy to, co zwróci metoda toDto z doctorMapper.
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return doctorMapper.toDto(doctor);
    }

    // 1. Kiedy lekarz o podanym identyfikatorze nie istnieje (doctorRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek.
    // 2. Kiedy lekarz istnieje (doctorRepository.findById zwraca optional zawierający lekarza), lekarz zostaje usunięty (doctorRepository.delete).
    @Transactional
    public void deleteDoctor(Long id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        Doctor doctor = optionalDoctor.orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        doctorRepository.delete(doctor);
    }

    // 1. Kiedy lekarz o podanym identyfikatorze nie istnieje (doctorRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek.
    // 2. Kiedy klinika o podanym identyfikatorze nie istnieje (clinicRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek.
    // 3. Kiedy lekarz i klinika istnieją, klinika jest dodawana do zbioru klinik lekarza, a lekarz zostaje zapisany (doctorRepository.save).
    @Transactional
    public void assignDoctor(Long doctorId, Long clinicId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));

        doctor.getClinics().add(clinic);
        doctorRepository.save(doctor);
    }
}