package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.exceptions.appointmentEx.AppointmentNotFoundException;
import com.wojteknier03.clinicmedical.exceptions.doctorEx.DoctorNotFoundException;
import com.wojteknier03.clinicmedical.mapper.AppointmentMapper;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.model.Appointment;
import com.wojteknier03.clinicmedical.model.Doctor;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.repository.AppointmentRepository;
import com.wojteknier03.clinicmedical.repository.DoctorRepository;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    AppointmentService appointmentService;
    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    AppointmentMapper appointmentMapper;

    @BeforeEach
    void setup() {
        appointmentRepository = Mockito.mock(AppointmentRepository.class);
        patientRepository = Mockito.mock(PatientRepository.class);
        doctorRepository = Mockito.mock(DoctorRepository.class);
        appointmentMapper = Mockito.mock(AppointmentMapper.class);
        appointmentService = new AppointmentService(appointmentRepository, patientRepository, doctorRepository, appointmentMapper);
    }

    @Test
    void getPatientAppointments_ExistingPatientId_ReturnAppointmentsDtoList() {
        Long patientId = 1L;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0)));
        appointments.add(createAppointment(2L, LocalDateTime.of(2023, 1, 1, 10, 15)));

        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientId(patientId)).thenReturn(appointments);
        when(appointmentMapper.toDtoList(appointments)).thenReturn(createAppointmentDtoList(appointments));

        List<AppointmentDto> result = appointmentService.getPatientAppointments(patientId);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getPatientAppointments_PatientNotFound_ExceptionThrown() {
        Long patientId = 1L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.getPatientAppointments(patientId));
    }

    @Test
    void bookAppointment_ValidAppointmentDto_ReturnAppointmentDto() {
        Long patientId = 1L;
        AppointmentDto appointmentDto = createAppointmentDto(1L, LocalDateTime.of(2023, 1, 1, 10, 0));
        Appointment appointment = createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0));
        Patient patient = new Patient();
        patient.setId(patientId);

        when(appointmentMapper.fromDto(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.existsByStartTime(appointment.getStartTime())).thenReturn(false);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        AppointmentDto result = appointmentService.bookAppointment(appointmentDto, patientId);

        assertEquals(1L, result.getId());
    }

    @Test
    void bookAppointment_AppointmentAlreadyExists_ExceptionThrown() {
        Long patientId = 1L;
        AppointmentDto appointmentDto = createAppointmentDto(1L, LocalDateTime.of(2023, 1, 1, 10, 0));
        Appointment appointment = createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0));

        when(appointmentMapper.fromDto(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.existsByStartTime(appointment.getStartTime())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> appointmentService.bookAppointment(appointmentDto, patientId));
    }

    @Test
    void bookAppointment_PatientNotFound_ExceptionThrown() {
        Long patientId = 1L;
        AppointmentDto appointmentDto = createAppointmentDto(1L, LocalDateTime.of(2023, 1, 1, 10, 0));
        Appointment appointment = createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0));

        when(appointmentMapper.fromDto(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.existsByStartTime(appointment.getStartTime())).thenReturn(false);
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.bookAppointment(appointmentDto, patientId));
    }

    @Test
    void getAvailableAppointmentsForDoctor_ExistingDoctorId_ReturnAppointmentsDtoList() {
        Long doctorId = 1L;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0)));
        appointments.add(createAppointment(2L, LocalDateTime.of(2023, 1, 1, 10, 15)));

        Page<Appointment> appointmentPage = new PageImpl<>(appointments);
        Pageable pageable = PageRequest.of(0, 10);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(new Doctor()));
        when(appointmentRepository.findByDoctorIdAndPatientIsNull(doctorId, pageable)).thenReturn(appointmentPage);
        when(appointmentMapper.toDtoList(appointments)).thenReturn(createAppointmentDtoList(appointments));

        List<AppointmentDto> result = appointmentService.getAvailableAppointmentsForDoctor(doctorId, pageable);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getAvailableAppointmentsForDoctor_DoctorNotFound_ExceptionThrown() {
        Long doctorId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.getAvailableAppointmentsForDoctor(doctorId, pageable));
    }

    @Test
    void getAvailableAppointmentsForSpecializationAndDay_ValidSpecializationAndDate_ReturnAppointmentsDtoList() {
        String specialization = "Cardiology";
        LocalDate date = LocalDate.of(2023, 1, 1);
        List<Doctor> doctors = new ArrayList<>();
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctors.add(doctor);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0)));
        appointments.add(createAppointment(2L, LocalDateTime.of(2023, 1, 1, 10, 15)));

        Page<Appointment> appointmentPage = new PageImpl<>(appointments);
        Pageable pageable = PageRequest.of(0, 10);

        when(doctorRepository.findBySpecialization(specialization)).thenReturn(doctors);
        when(appointmentRepository.findByDoctorInAndStartTimeBetweenAndPatientIsNull(doctors, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), pageable)).thenReturn(appointmentPage);
        when(appointmentMapper.toDtoList(appointments)).thenReturn(createAppointmentDtoList(appointments));

        List<AppointmentDto> result = appointmentService.getAvailableAppointmentsForSpecializationAndDay(specialization, date, pageable);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getAvailableAppointmentsForSpecializationAndDay_InvalidSpecialization_ExceptionThrown() {
        String specialization = "InvalidSpecialization";
        LocalDate date = LocalDate.of(2023, 1, 1);
        Pageable pageable = PageRequest.of(0, 10);

        when(doctorRepository.findBySpecialization(specialization)).thenReturn(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.getAvailableAppointmentsForSpecializationAndDay(specialization, date, pageable));
    }

    @Test
    void assignDoctorToAppointment_Correct() {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        appointmentService.assignDoctorToAppointment(appointmentId, doctorId);

        verify(appointmentRepository).save(appointment);
        assertEquals(doctor, appointment.getDoctor());
    }

    @Test
    void assignDoctorToAppointment_AppointmentNotFound_ExceptionThrown() {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
        });
    }

    @Test
    void assignDoctorToAppointment_DoctorNotFound_ExceptionThrown() {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
        });
    }
    private Appointment createAppointment(Long id, LocalDateTime startTime) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStartTime(startTime);
        appointment.setEndTime(startTime.plusHours(1));
        return appointment;
    }

    private AppointmentDto createAppointmentDto(Long id, LocalDateTime startTime) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(id);
        appointmentDto.setStartTime(startTime);
        appointmentDto.setEndTime(startTime.plusHours(1));
        return appointmentDto;
    }

    private List<AppointmentDto> createAppointmentDtoList(List<Appointment> appointments) {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(createAppointmentDto(appointment.getId(), appointment.getStartTime()));
        }
        return appointmentDtos;
    }

    private Patient createPatient(String email, Long id) {
        AppUser user = new AppUser();
        user.setId(id);
        List<Appointment> appointments = new ArrayList<>();
        return new Patient(id, email, "213213", "kgfdsk", "lasdlas", "9329392",
                LocalDate.of(2012, 1, 5), appointments, user);
    }
}
