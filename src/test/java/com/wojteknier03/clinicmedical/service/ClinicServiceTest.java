package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.ClinicDto;
import com.wojteknier03.clinicmedical.mapper.ClinicMapper;
import com.wojteknier03.clinicmedical.model.Clinic;
import com.wojteknier03.clinicmedical.repository.ClinicRepository;
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
        //given
        ClinicDto clinicDto = createClinicDto(1L);
        Clinic clinic = createClinic(1L);

        //when
        when(clinicMapper.fromDto(clinicDto)).thenReturn(clinic);
        when(clinicRepository.save(clinic)).thenReturn(clinic);
        when(clinicMapper.toDto(clinic)).thenReturn(clinicDto);

        //then
        ClinicDto result = clinicService.addClinic(clinicDto);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void addClinic_InvalidClinicDto_ExceptionThrown(){
        // given
        ClinicDto clinicDto = createClinicDto(1L);
        Clinic clinic = createClinic(1L);

       //when
        when(clinicMapper.fromDto(clinicDto)).thenReturn(clinic);
        when(clinicRepository.save(clinic)).thenThrow(RuntimeException.class);

        //then
        Assertions.assertThrows(RuntimeException.class, () -> {
            clinicService.addClinic(clinicDto);
        });
    }

    @Test
    void getClinics_ReturnClinicsDtoList(){
        //given
        List<Clinic> clinics = new ArrayList<>();
        clinics.add(createClinic(1L));
        clinics.add(createClinic(2L));

        when(clinicRepository.findAll()).thenReturn(clinics);
        when(clinicMapper.toDtoList(clinics)).thenReturn(createClinicDtoList(clinics));

        //when
        List<ClinicDto> result = clinicService.getClinics(Pageable.unpaged());

        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getClinics_NoClinicsFound_EmptyListReturned(){
        //given
        List<Clinic> clinics = new ArrayList<>();

        when(clinicRepository.findAll()).thenReturn(clinics);
        when(clinicMapper.toDtoList(clinics)).thenReturn(createClinicDtoList(clinics));

        //when
        List<ClinicDto> result = clinicService.getClinics(Pageable.unpaged());

        //then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getClinicById_ExistingClinicId_ReturnClinicDto(){
        //given
        Clinic clinic = createClinic(1L);
        ClinicDto clinicDto = createClinicDto(1L);

        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));
        when(clinicMapper.toDto(clinic)).thenReturn(clinicDto);

        //when
        ClinicDto result = clinicService.getClinicById(1L);

        //then
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getClinicById_NonExistingClinicId_ExceptionThrown(){
        //given
        Long nonExistingId = 999L;

        when(clinicRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clinicService.getClinicById(nonExistingId);
        });
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
