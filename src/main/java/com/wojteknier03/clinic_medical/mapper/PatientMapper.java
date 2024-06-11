package com.wojteknier03.clinic_medical.mapper;

import com.wojteknier03.clinic_medical.dto.PatientDto;
import com.wojteknier03.clinic_medical.dto.UserDto;
import com.wojteknier03.clinic_medical.model.AppUser;
import com.wojteknier03.clinic_medical.model.Appointment;
import com.wojteknier03.clinic_medical.model.Patient;
import org.h2.engine.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "user.id", target = "userId")
    PatientDto patientToPatientDto(Patient patient);
    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    Patient patientDtoToPatient(PatientDto patientDto);

    @Named("userIdToUser")
    default AppUser userIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        AppUser user = new AppUser();
        user.setId(userId);
        return user;
    }
}
