package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.mapper.AppointmentMapper;
import com.wojteknier03.clinicmedical.model.Appointment;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.repository.AppointmentRepository;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    //1. Kiedy spotkanie o podanym czasie rozpoczęcia już istnieje (czyli appointmentRepository.existsByStartTime zwraca true) powinien zostać rzucony wyjątek
    //2. Kiedy czasy rozpoczęcia/zakończenia są nieprawidłowe walidacja rzuca wyjątek
    //3. Kiedy spotkanie o podanym czasie rozpoczęcia nie istnieje (czyli appointmentRepository.existsByStartTime zwraca false), spotkanie zostaje dodane
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

    //1. Pobranie stronicowanej listy spotkań (czyli appointmentRepository.findAll(pageable).getContent zwraca listę spotkań)
    //2. Zwracamy to, co zwróci metoda toDtoList z appointmentMapper
    public List<AppointmentDto> getAppointments(Pageable pageable) {
        List<Appointment> appointments = appointmentRepository.findAll(pageable).getContent();
        return appointmentMapper.toDtoList(appointments);
    }

    //1. Kiedy pacjent nie istnieje (czyli patientRepository.findById zwroci optional.empty) to powinien zostac rzucony wyjatek
    //2. Kiedy pacjent istnieje (czyli patientRepository.findById zwroci optionala, w ktorm znajduje sie jakis pacjent), wizyta rowniez istnieje
    // (czyli appointmentRepository zwraca liste appointments), to zwracamy to co zwroci metoda toDtoList z appointmentMapper
    public List<AppointmentDto> getAppointmentByPatientId(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointmentMapper.toDtoList(appointments);
    }

    //1. Kiedy spotkanie nie istnieje (czyli appointmentRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek
    //2. Kiedy pacjent nie istnieje (czyli patientRepository.findById zwraca Optional.empty) powinien zostać rzucony wyjątek
    //3. Kiedy spotkanie i pacjent istnieją, pacjent jest przypisany do spotkania, a spotkanie zostaje zapisane (appointmentRepository.save)
    @Transactional
    public void assignPatientToAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        appointment.setPatient(patient);
        appointmentRepository.save(appointment);
    }

    //1. Kiedy czas rozpoczęcia lub zakończenia wizyty nie są w pełnych 15 minutach (metoda sprawdza, czy czas rozpoczęcia (appointment.getStartTime().getMinute())
    //   lub czas zakończenia (appointment.getEndTime().getMinute()) nie są podzielne przez 15) metoda rzuca wyjątek
    //2. Kiedy czas zakończenia wizyty nie jest po czasie rozpoczęcia (!appointment.getEndTime().isAfter(appointment.getStartTime()) to metoda rzuca wyjątek
    //3. Jeśli zarówno czas rozpoczęcia (appointment.getStartTime().getMinute()) jak i czas zakończenia (appointment.getEndTime().getMinute()) są w pełnych 15 minutach
    //   (są podzielne przez 15), a czas zakończenia wizyty (appointment.getEndTime()) nie jest przed czasem rozpoczęcia (appointment.getStartTime()).
    //   to walidacja przechodzi pomyślnie i metoda nie rzuca wyjątku.
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