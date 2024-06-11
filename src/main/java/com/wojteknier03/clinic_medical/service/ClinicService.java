package com.wojteknier03.clinic_medical.service;

import com.wojteknier03.clinic_medical.dto.ClinicDto;
import com.wojteknier03.clinic_medical.mapper.ClinicMapper;
import com.wojteknier03.clinic_medical.model.Clinic;
import com.wojteknier03.clinic_medical.repository.ClinicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    @Transactional
    public ClinicDto addClinic(ClinicDto clinicDto) {
        Clinic clinic = clinicMapper.fromDto(clinicDto);
        Clinic savedClinic = clinicRepository.save(clinic);
        return clinicMapper.toDto(savedClinic);
    }

    public List<ClinicDto> getClinics(Pageable pageable){
        List<Clinic> clinics = clinicRepository.findAll(pageable).getContent();
        return clinicMapper.toDtoList(clinics);
    }

    public ClinicDto getClinicById(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));
        return clinicMapper.toDto(clinic);
    }

    @Transactional
    public void deleteClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));
        clinicRepository.delete(clinic);
    }
}
