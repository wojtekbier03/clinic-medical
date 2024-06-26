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
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class AppointmentServiceTest {

    AppointmentService appointmentService;
    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    AppointmentMapper appointmentMapper;

    @BeforeEach
    void setup() {
        appointmentRepository =  Mockito.mock(AppointmentRepository.class);
        patientRepository = Mockito.mock(PatientRepository.class);
        appointmentMapper = Mappers.getMapper(AppointmentMapper.class);
        appointmentService = new AppointmentService(appointmentRepository, patientRepository, appointmentMapper);
    }

    @Test
    void addAppointment_ValidAppointmentDto_ReturnAppointmentDto() {
        //given
        AppointmentDto appointmentDto = createAppointmentDto(1L);
        Appointment appointment = createAppointment(1L);

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
        AppointmentDto appointmentDto = createAppointmentDto(1L);
        Appointment appointment = createAppointment(1L);

        when(appointmentMapper.fromDto(appointmentDto)).thenReturn(appointment);
        when(appointmentRepository.existsByStartTime(appointment.getStartTime())).thenReturn(true);

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> appointmentService.addAppointment(appointmentDto));
    }

    @Test
    void getAppointments_ReturnAppointmentsDtoList() {
        //given
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(1L));
        appointments.add(createAppointment(2L));

        when(appointmentRepository.findAll()).thenReturn(appointments);
        when(appointmentMapper.toDtoList(appointments)).thenReturn(createAppointmentDtoList(appointments));

        //when
        List<AppointmentDto> result = appointmentService.getAppointments(Pageable.unpaged());

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
        appointments.add(createAppointment(1L));
        appointments.add(createAppointment(2L));

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

    private Appointment createAppointment(Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStartTime(LocalDateTime.now());
        appointment.setEndTime(LocalDateTime.now().plusHours(1));
        return appointment;
    }

    private AppointmentDto createAppointmentDto(Long id) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(id);
        appointmentDto.setStartTime(LocalDateTime.now());
        appointmentDto.setEndTime(LocalDateTime.now().plusHours(1));
        return appointmentDto;
    }

    private List<AppointmentDto> createAppointmentDtoList(List<Appointment> appointments) {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(createAppointmentDto(appointment.getId()));
        }
        return appointmentDtos;
    }
}