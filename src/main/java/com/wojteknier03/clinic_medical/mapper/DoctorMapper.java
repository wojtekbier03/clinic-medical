package com.wojteknier03.clinic_medical.mapper;

import com.wojteknier03.clinic_medical.dto.DoctorDto;
import com.wojteknier03.clinic_medical.model.Clinic;
import com.wojteknier03.clinic_medical.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "clinicIds", source = "clinics")
    DoctorDto toDto(Doctor doctor);

    @Mapping(target = "clinics", ignore = true)
    Doctor fromDto(DoctorDto doctorDto);

    default Set<Long> map(Set<Clinic> clinics) {
        return clinics.stream()
                .map(Clinic::getId)
                .collect(Collectors.toSet());
    }

    List<DoctorDto> toDtoList(List<Doctor> doctors);
}
