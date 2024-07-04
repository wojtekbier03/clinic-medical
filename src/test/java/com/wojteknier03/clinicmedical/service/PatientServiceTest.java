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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setup(){
        patientMapper = Mappers.getMapper(PatientMapper.class);
        patientService = new PatientService(patientRepository, patientMapper, userRepository);
    }

//    @Test
//    void getPatientByEmail_PatientExists_ReturnPatientDto() {
//        // Given
//        String email = "email";
//        Patient patient = createPatient(email, 1L);
//        PatientDto expectedDto = new PatientDto();
//        expectedDto.setEmail(email);
//
//        // Mock repository behavior
//        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
//
//        // Mock mapper behavior
//        when(patientMapper.patientToPatientDto(patient)).thenReturn(expectedDto);
//
//        // When
//        PatientDto result = patientService.getPatientByEmail(email);
//
//        // Then
//        Assertions.assertEquals(email, result.getEmail());
//    }

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

//    @Test
//    void add_ValidPatientDto_ReturnPatientDto() {
//        //given
//        String email = "newpatient@example.com";
//        PatientDto patientDto = new PatientDto();
//        patientDto.setEmail(email);
//        patientDto.setUserId(1L);
//
//        AppUser user = new AppUser();
//        user.setId(1L);
//
//        Patient patient = new Patient();
//        patient.setEmail(email);
//        patient.setUser(user);
//
//        Patient savedPatient = new Patient();
//        savedPatient.setId(1L);
//        savedPatient.setEmail(email);
//        savedPatient.setUser(user);
//
//        // Mockowanie UserRepository
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // Mockowanie PatientMapper
//        when(patientMapper.patientDtoToPatient(patientDto)).thenReturn(patient);
//        when(patientMapper.patientToPatientDto(savedPatient)).thenReturn(patientDto);
//
//        // Mockowanie PatientRepository
//        when(patientRepository.save(patient)).thenReturn(savedPatient);
//
//        // Wywołanie metody w serwisie
//        PatientDto result = patientService.add(patientDto);
//
//        // Sprawdzenie wyniku
//        Assertions.assertEquals(email, result.getEmail());
//        Assertions.assertEquals(1L, result.getUserId());
//    }

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

//    @Test
//    void update_ExistingPatient_ReturnUpdatedPatientDto() {
//        // given
//        String email = "patient@example.com";
//        PatientDto updatedDto = new PatientDto();
//        updatedDto.setEmail(email);
//
//        Patient patient = createPatient(email, 1L);
//
//        // Mockowanie patientRepository
//        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
//
//        // Mockowanie patientMapper
//        when(patientMapper.patientToPatientDto(patient)).thenReturn(updatedDto);
//
//        // when
//        PatientDto result = patientService.update(email, updatedDto);
//
//        // then
//        Assertions.assertEquals(email, result.getEmail());
//    }

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
