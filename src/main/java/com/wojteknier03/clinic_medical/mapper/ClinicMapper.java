package com.wojteknier03.clinic_medical.mapper;

import com.wojteknier03.clinic_medical.dto.ClinicDto;
import com.wojteknier03.clinic_medical.model.Clinic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    ClinicDto toDto(Clinic clinic);
    Clinic fromDto(ClinicDto clinicDto);
    List<ClinicDto> toDtoList(List<Clinic> clinics);
}
