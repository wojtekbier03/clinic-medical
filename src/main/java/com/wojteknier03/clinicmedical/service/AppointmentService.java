package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.exceptions.appointmentEx.AppointmentNotFoundException;
import com.wojteknier03.clinicmedical.exceptions.doctorEx.DoctorNotFoundException;
import com.wojteknier03.clinicmedical.mapper.AppointmentMapper;
import com.wojteknier03.clinicmedical.model.Appointment;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.model.Doctor;
import com.wojteknier03.clinicmedical.repository.AppointmentRepository;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import com.wojteknier03.clinicmedical.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional
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

    public List<AppointmentDto> getAppointments(Pageable pageable) {
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

    @Transactional
    public void assignPatientToAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        appointment.setPatient(patient);
        appointmentRepository.save(appointment);
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment.getStartTime().getMinute() % 15 != 0 || appointment.getEndTime().getMinute() % 15 != 0) {
            throw new IllegalArgumentException("Appointment start time must be on the quarter hour");
        }
        if (!appointment.getEndTime().isAfter(appointment.getStartTime())) {
            throw new IllegalArgumentException("Appointment end time must be after start time");
        }
    }

    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointmentMapper.toDtoList(appointments);
    }

    @Transactional
    public AppointmentDto bookAppointment(AppointmentDto appointmentDto, Long patientId) {
        Appointment appointment = appointmentMapper.fromDto(appointmentDto);

        if (appointmentRepository.existsByStartTime(appointment.getStartTime())) {
            throw new IllegalArgumentException("Appointment already exists at the specified start time");
        }

        validateAppointment(appointment);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        appointment.setPatient(patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }

    public List<AppointmentDto> getAvailableAppointmentsForDoctor(Long doctorId, Pageable pageable) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndPatientIsNull(doctorId, pageable).getContent();
        return appointmentMapper.toDtoList(appointments);
    }

    public List<AppointmentDto> getAvailableAppointmentsForSpecializationAndDay(String specialization, LocalDate date, Pageable pageable) {
        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
        if (doctors.isEmpty()) {
            throw new IllegalArgumentException("No doctors found with the given specialization");
        }
        List<Appointment> appointments = appointmentRepository.findByDoctorInAndStartTimeBetweenAndPatientIsNull(
                doctors,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                pageable
        ).getContent();
        return appointmentMapper.toDtoList(appointments);
    }

    @Transactional
    public void assignDoctorToAppointment(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        appointment.setDoctor(doctor);
        appointmentRepository.save(appointment);
    }
}
