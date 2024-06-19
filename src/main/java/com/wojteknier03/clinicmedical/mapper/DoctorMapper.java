package com.wojteknier03.clinicmedical.mapper;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.model.Doctor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);
    Doctor fromDto(DoctorDto doctorDto);
    List<DoctorDto> toDtoList(List<Doctor> doctors);
}
