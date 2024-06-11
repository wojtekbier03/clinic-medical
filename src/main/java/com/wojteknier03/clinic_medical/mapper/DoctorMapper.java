package com.wojteknier03.clinic_medical.mapper;

import com.wojteknier03.clinic_medical.dto.DoctorDto;
import com.wojteknier03.clinic_medical.model.Clinic;
import com.wojteknier03.clinic_medical.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "clinicIds", source = "clinics", qualifiedByName = "clinicsToClinicIds")
    DoctorDto toDto(Doctor doctor);

    @Mapping(target = "clinics", ignore = true)
    Doctor fromDto(DoctorDto doctorDto);

    @Named("clinicsToClinicIds")
    default Set<Long> clinicsToClinicIds (Set<Clinic> clinics) {
        return clinics.stream()
                .map(Clinic::getId)
                .collect(Collectors.toSet());
    }

    default List<DoctorDto> toDtoList(List<Doctor> doctors){
        return doctors.stream()
                .map(this::toDto)
                .toList();
    }
}
