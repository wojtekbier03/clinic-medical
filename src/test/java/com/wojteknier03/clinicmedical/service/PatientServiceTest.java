package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.mapper.PatientMapper;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import com.wojteknier03.clinicmedical.repository.UserRepository;
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
        //given
        String email = "email";
        Patient patient = createPatient(email, 1L);
        PatientDto expectedDto = new PatientDto();
        expectedDto.setEmail(email);

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientDto(patient)).thenReturn(expectedDto);

        //when
        PatientDto result = patientService.getPatientByEmail(email);

        //then
        Assertions.assertEquals(email, result.getEmail());
    }

    @Test
    void getPatientByEmail_PatientNotExists_ExceptionThrown() {
        //given
        String email = "nonexistent@example.com";

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            patientService.getPatientByEmail(email);
        });
    }

    @Test
    void add_ValidPatientDto_ReturnPatientDto() {
        //given
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

        //when
        PatientDto result = patientService.add(patientDto);

        //then
        Assertions.assertEquals(email, result.getEmail());
        Assertions.assertEquals(1L, result.getUserId());
    }

    @Test
    void add_PatientEmailAlreadyExists_ExceptionThrown() {
        //given
        String email = "existingpatient@example.com";
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail(email);

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(new Patient()));

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            patientService.add(patientDto);
        });
    }

    @Test
    void add_UserNotFound_ExceptionThrown() {
        //given
        String email = "newpatient@example.com";
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail(email);
        patientDto.setUserId(999L); // Non-existing user ID

        //when
        when(userRepository.findById(patientDto.getUserId())).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            patientService.add(patientDto);
        });
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

        //when
        PatientDto result = patientService.update(email, updatedDto);

        //then
        Assertions.assertEquals(email, result.getEmail());
    }

    @Test
    void update_PatientNotFound_ExceptionThrown() {
        //given
        String email = "nonexistent@example.com";
        PatientDto updatedDto = new PatientDto();
        updatedDto.setEmail(email);

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            patientService.update(email, updatedDto);
        });
    }

    private Patient createPatient(String email, Long id) {
        AppUser user = new AppUser();
        user.setId(id);
        return new Patient(id, email, "213213", "kgfdsk", "lasdlas", "9329392",
                LocalDate.of(2012, 1, 5), user);
    }
}