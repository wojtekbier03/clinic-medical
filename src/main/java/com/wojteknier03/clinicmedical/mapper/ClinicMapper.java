package com.wojteknier03.clinicmedical.mapper;

import com.wojteknier03.clinicmedical.dto.ClinicDto;
import com.wojteknier03.clinicmedical.model.Clinic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    ClinicDto toDto(Clinic clinic);
    Clinic fromDto(ClinicDto clinicDto);
    List<ClinicDto> toDtoList(List<Clinic> clinics);
}
