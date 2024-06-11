package com.wojteknier03.clinic_medical.service;

import com.wojteknier03.clinic_medical.dto.AppointmentDto;
import com.wojteknier03.clinic_medical.mapper.AppointmentMapper;
import com.wojteknier03.clinic_medical.model.Appointment;
import com.wojteknier03.clinic_medical.model.Patient;
import com.wojteknier03.clinic_medical.repository.AppointmentRepository;
import com.wojteknier03.clinic_medical.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentMapper.fromDto(appointmentDto);

        LocalDateTime startTime = appointment.getStartTime();
        if (appointmentRepository.existsByStartTime(startTime)) {
            throw new IllegalArgumentException("Appointment already exists at the specified start time");
        }

        validateAppointment(appointment);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }

    public List<AppointmentDto> getAppointments(Pageable pageable){
        List<Appointment> appointments = appointmentRepository.findAll(pageable).getContent();
        return appointmentMapper.toDtoList(appointments);
    }

    public List<AppointmentDto> getAppointmentByPatientId(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointmentMapper.toDtoList(appointments);
    }

    public void assignPatientToAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        appointment.setPatient(patient);
        appointmentRepository.save(appointment);
    }

    private void validateAppointment(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        if (appointment.getStartTime().getMinute() % 15 != 0 || appointment.getEndTime().getMinute() % 15 != 0) {
            throw new IllegalArgumentException("Appointment start time must be on the quarter hour");
        }
        if (!appointment.getEndTime().isAfter(appointment.getStartTime())) {
            throw new IllegalArgumentException("Appointment end time must be after start time");
        }
    }
}