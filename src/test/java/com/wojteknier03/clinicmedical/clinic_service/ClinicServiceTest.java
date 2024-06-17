package com.wojteknier03.clinicmedical.clinic_service;

import com.wojteknier03.clinicmedical.dto.ClinicDto;
import com.wojteknier03.clinicmedical.mapper.ClinicMapper;
import com.wojteknier03.clinicmedical.model.Clinic;
import com.wojteknier03.clinicmedical.repository.ClinicRepository;
import com.wojteknier03.clinicmedical.service.ClinicService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class ClinicServiceTest {

    ClinicService clinicService;
    ClinicRepository clinicRepository;
    ClinicMapper clinicMapper;

    @BeforeEach
    void setup(){
        clinicRepository = Mockito.mock(ClinicRepository.class);
        clinicMapper = Mappers.getMapper(ClinicMapper.class);
        clinicService = new ClinicService(clinicRepository, clinicMapper);
    }

    @Test
    void addClinic_ValidClinicDto_ReturnClinicDto(){
        ClinicDto clinicDto = createClinicDto(1L);
        Clinic clinic = createClinic(1L);

        when(clinicMapper.fromDto(clinicDto)).thenReturn(clinic);
        when(clinicRepository.save(clinic)).thenReturn(clinic);
        when(clinicMapper.toDto(clinic)).thenReturn(clinicDto);

        ClinicDto result = clinicService.addClinic(clinicDto);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getClinics_ReturnClinicsDtoList(){
        List<Clinic> clinics = new ArrayList<>();
        clinics.add(createClinic(1L));
        clinics.add(createClinic(2L));

        when(clinicRepository.findAll()).thenReturn(clinics);
        when(clinicMapper.toDtoList(clinics)).thenReturn(createClinicDtoList(clinics));

        List<ClinicDto> result = clinicService.getClinics(Pageable.unpaged());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getClinicById_ExistingClinicId_ReturnClinicDto(){
        Clinic clinic = createClinic(1L);
        ClinicDto clinicDto = createClinicDto(1L);

        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));
        when(clinicMapper.toDto(clinic)).thenReturn(clinicDto);

        ClinicDto result = clinicService.getClinicById(1L);

        Assertions.assertEquals(1L, result.getId());
    }

    private Clinic createClinic(Long id) {
        Clinic clinic = new Clinic();
        clinic.setId(id);
        return clinic;
    }

    private ClinicDto createClinicDto(Long id) {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setId(id);
        return clinicDto;
    }

    private List<ClinicDto> createClinicDtoList(List<Clinic> clinics) {
        List<ClinicDto> clinicDtos = new ArrayList<>();
        for (Clinic clinic : clinics) {
            clinicDtos.add(createClinicDto(clinic.getId()));
        }
        return clinicDtos;
    }
}
