package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.mapper.PatientMapper;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import com.wojteknier03.clinicmedical.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    // 1. Jeśli pacjent nie istnieje (patientRepository.findById zwraca Optional.empty), rzucamy wyjątek
    // 2. Jeśli pacjent istnieje, zamieniamy go na PatientDto za pomocą metody patientToPatientDto z patientMapper (.map(patientMapper::patientToPatientDto))
    public PatientDto getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .map(patientMapper::patientToPatientDto)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    // 1. Sprawdzanie, czy email już istnieje (metoda checkEmail)
    // 2. Pobranie użytkownika na podstawie userId (userRepository.findById)
    // 3. Jeśli użytkownik nie istnieje(), rzucamy wyjątek
    // 4. Zamiana PatientDto na Patient za pomocą metody patientDtoToPatient z patientMapper
    // 5. Ustawienie użytkownika dla pacjenta (userRepository.findById(patientDto.getUserId()))
    // 6. Zapisanie pacjenta w patientRepository (patientRepository.save(patient))
    // 7. Zamiana zapisanego pacjenta na PatientDto za pomocą metody patientToPatientDto
    @Transactional
    public PatientDto add(PatientDto patientDto) {
        checkEmail(patientDto.getEmail());
        AppUser user = userRepository.findById(patientDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Patient patient = patientMapper.patientDtoToPatient(patientDto);
        patient.setUser((AppUser) user);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.patientToPatientDto(patientRepository.save(patient));
    }

    // 1. Pobieranie pacjenta za pomocą emaila (patientRepository.findByEmail)
    // 2. Jeśli pacjent nie istnieje(patientRepository.findByEmail daje Optional.empty), rzucamy wyjątek
    // 3. Usunięcie pacjenta z patientRepository (patientRepository.delete(patient))
    @Transactional
    public void delete(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patientRepository.delete(patient);
    }

    // 1. Pobieranie pacjenta za pomocą emaila (patientRepository.findByEmail)
    // 2. Jeśli pacjent nie istnieje(patientRepository.findByEmail daje Optional.empty), rzucamy wyjątek
    // 3. Aktualizacja danych pacjenta na podstawie updatedPatientDto (patient.updateFromDto)
    // 4. Zapisanie zaktualizowanego pacjenta w patientRepository (patientRepository.save(patient))
    // 5. Zamiana zaktualizowanego pacjenta na PatientDto za pomocą metody patientToPatientDto
    @Transactional
    public PatientDto update(String email, PatientDto updatedPatientDto) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        patient.updateFromDto(updatedPatientDto);

        return patientMapper.patientToPatientDto(patientRepository.save(patient));
    }

    // 1. Sprawdzenie, czy pacjent o podanym emailu już istnieje (patientRepository.findByEmail)
    // 2. Jeśli pacjent istnieje (findByID(email).isPresent() daje true), rzucamy wyjątek
    private void checkEmail(String email) {
        if (patientRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }
    }
}