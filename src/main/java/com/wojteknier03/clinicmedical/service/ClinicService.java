package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.ClinicDto;
import com.wojteknier03.clinicmedical.mapper.ClinicMapper;
import com.wojteknier03.clinicmedical.model.Clinic;
import com.wojteknier03.clinicmedical.repository.ClinicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    //1.Zamiana obiektu ClinicDto na obiekt Clinic za pomocą metody fromDto z clinicMapper (clinicMapper.fromDto(clinicDto)).
    //2.Zapisanie obiektu Clinic w clinicRepository ( clinicRepository.save(clinic)).
    //3.Zamiana zapisanego obiektu Clinic na obiekt ClinicDto za pomocą metody toDto z clinicMapper
    //  (clinicMapper.toDto(savedClinic)
    @Transactional
    public ClinicDto addClinic(ClinicDto clinicDto) {
        Clinic clinic = clinicMapper.fromDto(clinicDto);
        Clinic savedClinic = clinicRepository.save(clinic);
        return clinicMapper.toDto(savedClinic);
    }

    //1. Pobramy listę klinik (clinicRepository.findAll(pageable).getContent zwraca listę klinik)
    //2. Zwracamy to, co zwróci metoda toDtoList z clinicMapper.
    public List<ClinicDto> getClinics(Pageable pageable){
        List<Clinic> clinics = clinicRepository.findAll(pageable).getContent();
        return clinicMapper.toDtoList(clinics);
    }

    // 1. Kiedy klinika o podanym identyfikatorze nie istnieje (clinicRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek.
    // 2. Kiedy klinika istnieje (clinicRepository.findById zwraca optional zawierający klinikę), zwracamy to, co zwróci metoda toDto z clinicMapper.
    public ClinicDto getClinicById(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));
        return clinicMapper.toDto(clinic);
    }

    // 1. Kiedy klinika o podanym identyfikatorze nie istnieje (clinicRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek.
    // 2. Kiedy klinika istnieje (clinicRepository.findById zwraca optional zawierający klinikę), klinika zostaje usunięta (clinicRepository.delete).
    @Transactional
    public void deleteClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));
        clinicRepository.delete(clinic);
    }
}
