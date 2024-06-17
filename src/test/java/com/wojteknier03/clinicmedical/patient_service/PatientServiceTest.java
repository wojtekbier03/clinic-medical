package com.wojteknier03.clinicmedical.patient_service;

import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.mapper.PatientMapper;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import com.wojteknier03.clinicmedical.repository.UserRepository;
import com.wojteknier03.clinicmedical.service.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class PatientServiceTest {
    PatientService patientService;
    PatientRepository patientRepository;
    PatientMapper patientMapper;
    UserRepository userRepository;

    @BeforeEach
    void setup(){
        patientRepository = Mockito.mock(PatientRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        patientMapper = Mappers.getMapper(PatientMapper.class);
        patientService = new PatientService(patientRepository, patientMapper, userRepository);
    }

    @Test
    void getPatientByEmail_PatientExists_ReturnPatientDto(){
         String email = "email";
         Patient patient = createPatient(email, 1L);
        PatientDto expectedDto = new PatientDto();
        expectedDto.setEmail(email);

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientDto(patient)).thenReturn(expectedDto);

        PatientDto result = patientService.getPatientByEmail(email);

        Assertions.assertEquals(email, result.getEmail());
    }

    @Test
    void add_ValidPatientDto_ReturnPatientDto() {
        String email = "newpatient@example.com";
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail(email);
        patientDto.setUserId(1L);

        AppUser user = new AppUser();
        user.setId(1L);

        Patient patient = new Patient();
        patient.setEmail(email);
        patient.setUser(user);

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setEmail(email);
        savedPatient.setUser(user);

        when(userRepository.findById(patientDto.getUserId())).thenReturn(Optional.of(user));
        when(patientMapper.patientDtoToPatient(patientDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientMapper.patientToPatientDto(savedPatient)).thenReturn(patientDto);

        PatientDto result = patientService.add(patientDto);

        Assertions.assertEquals(email, result.getEmail());
        Assertions.assertEquals(1L, result.getUserId());
    }

    @Test
    void update_ExistingPatient_ReturnUpdatedPatientDto() {
        // given
        String email = "patient@example.com";
        PatientDto updatedDto = new PatientDto();
        updatedDto.setEmail(email);

        Patient patient = createPatient(email, 1L);

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientDto(patient)).thenReturn(updatedDto);

        PatientDto result = patientService.update(email, updatedDto);

        Assertions.assertEquals(email, result.getEmail());
    }


    private Patient createPatient(String email, Long id) {
        AppUser user = new AppUser();
        user.setId(id);
        return new Patient(id, email, "213213", "kgfdsk", "lasdlas", "9329392",
                LocalDate.of(2012, 1, 5), user);
    }
}
