package com.wojteknier03.clinic_medical.mapper;

import com.wojteknier03.clinic_medical.dto.PatientDto;
import com.wojteknier03.clinic_medical.dto.UserDto;
import com.wojteknier03.clinic_medical.model.Patient;
import com.wojteknier03.clinic_medical.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "user.id", target = "userId")
    PatientDto patientToPatientDto(Patient patient);
    @Mapping(source = "userId", target = "user")
    Patient patientDtoToPatient(PatientDto patientDto);

    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);

    default User userIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
