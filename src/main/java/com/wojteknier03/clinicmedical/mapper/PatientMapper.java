package com.wojteknier03.clinicmedical.mapper;

import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "user.id", target = "userId")
    PatientDto patientToPatientDto(Patient patient);

    @Mapping(target = "user", ignore = true)
    Patient patientDtoToPatient(PatientDto patientDto);
}
