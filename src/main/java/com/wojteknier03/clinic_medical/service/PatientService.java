package com.wojteknier03.clinic_medical.service;

import com.wojteknier03.clinic_medical.dto.PatientDto;
import com.wojteknier03.clinic_medical.mapper.PatientMapper;
import com.wojteknier03.clinic_medical.model.AppUser;
import com.wojteknier03.clinic_medical.model.Patient;
import com.wojteknier03.clinic_medical.repository.PatientRepository;
import com.wojteknier03.clinic_medical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public PatientDto getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .map(patientMapper::patientToPatientDto)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    public PatientDto add(PatientDto patientDto) {
        checkEmail(patientDto.getEmail());
        AppUser user = userRepository.findById(patientDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Patient patient = patientMapper.patientDtoToPatient(patientDto);
        patient.setUser((AppUser) user);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.patientToPatientDto(patientRepository.save(patient));
    }

    public void delete(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patientRepository.delete(patient);
    }

    public PatientDto update(String email, PatientDto updatedPatientDto) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        patient.setEmail(updatedPatientDto.getEmail());
        patient.setFirstName(updatedPatientDto.getFirstName());
        patient.setLastName(updatedPatientDto.getLastName());
        patient.setIdCardNo(updatedPatientDto.getIdCardNo());
        patient.setPhoneNumber(updatedPatientDto.getPhoneNumber());
        patient.setBirthday(updatedPatientDto.getBirthday());

        return patientMapper.patientToPatientDto(patientRepository.save(patient));
    }

    private void checkEmail(String email) {
        if (patientRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }
    }
}