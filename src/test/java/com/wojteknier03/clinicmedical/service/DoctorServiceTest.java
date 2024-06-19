package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.mapper.DoctorMapper;
import com.wojteknier03.clinicmedical.model.Clinic;
import com.wojteknier03.clinicmedical.model.Doctor;
import com.wojteknier03.clinicmedical.repository.ClinicRepository;
import com.wojteknier03.clinicmedical.repository.DoctorRepository;
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
        //given
        DoctorDto doctorDto = createDoctorDto(1L);
        Doctor doctor = createDoctor(1L);
        Set<Long> clinicIds = new HashSet<>();
        clinicIds.add(1L);

        when(doctorMapper.fromDto(doctorDto)).thenReturn(doctor);
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(new Clinic()));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        //when
        DoctorDto result = doctorService.addDoctor(doctorDto);

        //then
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void addDoctor_InvalidClinicId_Exception(){
        //given
        DoctorDto doctorDto = createDoctorDto(1L);
        Doctor doctor = createDoctor(1L);
        Clinic clinic = new Clinic();
        Set<Long> clinicsId = new HashSet<>();
        clinicsId.add(999L);
        doctorDto.setClinicIds(clinicsId);

        when(doctorMapper.fromDto(doctorDto)).thenReturn(doctor);
        when(clinicRepository.findById(999L)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            doctorService.addDoctor(doctorDto);
        });
    }


    @Test
    void getAllDoctors_ReturnDoctorDtoList() {
        //given
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(createDoctor(1L));
        doctors.add(createDoctor(2L));

        when(doctorRepository.findAll(Pageable.unpaged())).thenReturn(new org.springframework.data.domain.PageImpl<>(doctors));
        when(doctorMapper.toDtoList(doctors)).thenReturn(createDoctorDtoList(doctors));


        //when
        List<DoctorDto> result = doctorService.getAllDoctors(Pageable.unpaged());

        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getDoctorById_ExistingDoctorId_ReturnDoctorDto() {
        //given
        Long doctorId = 1L;
        Doctor doctor = createDoctor(doctorId);
        DoctorDto doctorDto = createDoctorDto(doctorId);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        //when
        DoctorDto result = doctorService.getDoctorById(doctorId);

        //then
        Assertions.assertEquals(doctorId, result.getId());
    }

    @Test
    void getDoctorById_NonExistingDoctorId_ExceptionThrown() {
        //given
        Long doctorId = 999L;

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            doctorService.getDoctorById(doctorId);
        });
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

    private List<DoctorDto> createDoctorDtoList(List<Doctor> doctors) {
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorDtos.add(createDoctorDto(doctor.getId()));
        }
        return doctorDtos;
    }
}
