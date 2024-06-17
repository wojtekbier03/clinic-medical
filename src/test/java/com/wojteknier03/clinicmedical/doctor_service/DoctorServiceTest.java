package com.wojteknier03.clinicmedical.doctor_service;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.mapper.DoctorMapper;
import com.wojteknier03.clinicmedical.model.Clinic;
import com.wojteknier03.clinicmedical.model.Doctor;
import com.wojteknier03.clinicmedical.repository.ClinicRepository;
import com.wojteknier03.clinicmedical.repository.DoctorRepository;
import com.wojteknier03.clinicmedical.service.DoctorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    DoctorService doctorService;
    DoctorRepository doctorRepository;
    ClinicRepository clinicRepository;
    DoctorMapper doctorMapper;

    @BeforeEach
    void setup() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        clinicRepository = Mockito.mock(ClinicRepository.class);
        doctorMapper = Mappers.getMapper(DoctorMapper.class);
        doctorService = new DoctorService(doctorRepository, clinicRepository, doctorMapper);
    }

    @Test
    void addDoctor_ValidDoctorDto_ReturnDoctorDto() {
        DoctorDto doctorDto = createDoctorDto(1L);
        Doctor doctor = createDoctor(1L);
        Set<Long> clinicIds = new HashSet<>();
        clinicIds.add(1L);

        when(doctorMapper.fromDto(doctorDto)).thenReturn(doctor);
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(new Clinic()));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.addDoctor(doctorDto);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getAllDoctors_ReturnDoctorDtoList() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(createDoctor(1L));
        doctors.add(createDoctor(2L));

        List<DoctorDto> result = doctorService.getAllDoctors(Pageable.unpaged());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getDoctorById_ExistingDoctorId_ReturnDoctorDto() {
        Long doctorId = 1L;
        Doctor doctor = createDoctor(doctorId);
        DoctorDto doctorDto = createDoctorDto(doctorId);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.getDoctorById(doctorId);

        Assertions.assertEquals(doctorId, result.getId());
    }

    private Doctor createDoctor(Long id) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }

    private DoctorDto createDoctorDto(Long id) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(id);
        return doctorDto;
    }
}
