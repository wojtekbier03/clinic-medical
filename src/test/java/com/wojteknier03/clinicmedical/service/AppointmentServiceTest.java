package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.mapper.AppointmentMapper;
import com.wojteknier03.clinicmedical.model.Appointment;
import com.wojteknier03.clinicmedical.model.Patient;
import com.wojteknier03.clinicmedical.repository.AppointmentRepository;
import com.wojteknier03.clinicmedical.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    AppointmentService appointmentService;
    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    AppointmentMapper appointmentMapper;

    @BeforeEach
    void setup() {
        appointmentRepository = Mockito.mock(AppointmentRepository.class);
        patientRepository = Mockito.mock(PatientRepository.class);
        appointmentMapper = Mockito.mock(AppointmentMapper.class);
        appointmentService = new AppointmentService(appointmentRepository, patientRepository, appointmentMapper);
    }

    @Test
    void addAppointment_ValidAppointmentDto_ReturnAppointmentDto() {
        //given
        AppointmentDto appointmentDto = createAppointmentDto(1L, LocalDateTime.of(2023, 1, 1, 10, 0));
        Appointment appointment = createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0));

        when(appointmentMapper.fromDto(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.existsByStartTime(appointment.getStartTime())).thenReturn(false);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

        //when
        AppointmentDto result = appointmentService.addAppointment(appointmentDto);

        //then
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void addAppointment_AppointmentAlreadyExists_ExceptionThrown() {
        //given
        AppointmentDto appointmentDto = createAppointmentDto(1L, LocalDateTime.of(2023, 1, 1, 10, 0));
        Appointment appointment = createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0));

        when(appointmentMapper.fromDto(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.existsByStartTime(appointment.getStartTime())).thenReturn(true);

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> appointmentService.addAppointment(appointmentDto));
    }

    @Test
    void getAppointments_ReturnAppointmentsDtoList() {
        //given
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0)));
        appointments.add(createAppointment(2L, LocalDateTime.of(2023, 1, 1, 10, 15)));

        Page<Appointment> appointmentPage = new PageImpl<>(appointments);
        Pageable pageable = PageRequest.of(0, 10);

        when(appointmentRepository.findAll(pageable)).thenReturn(appointmentPage);
        when(appointmentMapper.toDtoList(appointments)).thenReturn(createAppointmentDtoList(appointments));

        //when
        List<AppointmentDto> result = appointmentService.getAppointments(pageable);

        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getAppointmentByPatientId_ExistingPatientId_ReturnAppointmentsDtoList() {
        //given
        Long patientId = 1L;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(1L, LocalDateTime.of(2023, 1, 1, 10, 0)));
        appointments.add(createAppointment(2L, LocalDateTime.of(2023, 1, 1, 10, 15)));

        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientId(patientId)).thenReturn(appointments);
        when(appointmentMapper.toDtoList(appointments)).thenReturn(createAppointmentDtoList(appointments));

        //when
        List<AppointmentDto> result = appointmentService.getAppointmentByPatientId(patientId);

        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getAppointmentByPatientId_PatientNotFound_ExceptionThrown() {
        // given
        Long patientId = 1L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> appointmentService.getAppointmentByPatientId(patientId));
    }

    @Test
    void assignPatientToAppointment_ValidIds_ExceptionThrown(){
        //given
        Patient patient = new Patient();
        Long appointmentId = 1L;
        Long patientId = 2L;
        Appointment appointment = createAppointment(appointmentId, LocalDateTime.of(2023, 1, 1, 10, 0));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        //when
        appointmentService.assignPatientToAppointment(appointmentId, patientId);

        //then
        verify(appointmentRepository, times(1)).save(appointment);
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void assignPatientToAppointment_AppointmentNotFound_ExceptionThrown(){
        //given
        Long appointmentId = 1L;
        Long patientId = 2L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> appointmentService.assignPatientToAppointment(appointmentId, patientId));

        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(patientRepository, times(0)).findById(patientId);
        verify(appointmentRepository, times(0)).save(any(Appointment.class));
    }

    @Test
    void assignPatientToAppointment_PatientNotFound_ExceptionThrown(){
        //given
        Long appointmentId = 1L;
        Long patientId = 2L;
        Appointment appointment = createAppointment(appointmentId, LocalDateTime.of(2023, 1, 1, 10, 0));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> appointmentService.assignPatientToAppointment(appointmentId, patientId));

        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(patientRepository, times(1)).findById(patientId);
        verify(appointmentRepository, times(0)).save(any(Appointment.class));
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
}
