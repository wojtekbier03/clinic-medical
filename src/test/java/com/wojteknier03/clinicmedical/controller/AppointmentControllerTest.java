package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.exceptions.InvalidPaginationParametersException;
import com.wojteknier03.clinicmedical.exceptions.appointmentEx.AppointmentNotFoundException;
import com.wojteknier03.clinicmedical.exceptions.appointmentEx.InvalidAppointmentDetailsException;
import com.wojteknier03.clinicmedical.exceptions.doctorEx.DoctorNotFoundException;
import com.wojteknier03.clinicmedical.exceptions.patientEx.PatientNotFoundException;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import com.wojteknier03.clinicmedical.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addAppointment_CorrectData_AppointmentSaved() throws Exception {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setStartTime(LocalDateTime.now());

        Mockito.when(appointmentService.addAppointment(any(AppointmentDto.class)))
                .thenReturn(appointmentDto);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentDto.getId()));
    }

    @Test
    void addAppointment_InvalidData_ThrowsException() throws Exception {
        AppointmentDto appointmentDto = new AppointmentDto();

        Mockito.when(appointmentService.addAppointment(any(AppointmentDto.class)))
                .thenThrow(new InvalidAppointmentDetailsException());

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAppointments_ReturnsListOfAppointments() throws Exception {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);

        Mockito.when(appointmentService.getAppointments(any()))
                .thenReturn(Collections.singletonList(appointmentDto));

        mockMvc.perform(get("/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(appointmentDto.getId()));
    }

    @Test
    void getAppointments_InvalidPagination_ThrowsException() throws Exception {
        Mockito.when(appointmentService.getAppointments(any()))
                .thenThrow(new InvalidPaginationParametersException());

        mockMvc.perform(get("/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void assignPatientToAppointment_CorrectData_AssignsPatient() throws Exception {
        Long appointmentId = 1L;
        Long patientId = 1L;

        mockMvc.perform(patch("/appointments/{appointmentId}/patients/{patientId}", appointmentId, patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(appointmentService, Mockito.times(1))
                .assignPatientToAppointment(appointmentId, patientId);
    }

    @Test
    void getPatientAppointments_CorrectData_ReturnAppointments() throws Exception {
        AppointmentDto appointmentDto1 = new AppointmentDto();
        appointmentDto1.setId(1L);
        appointmentDto1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));

        AppointmentDto appointmentDto2 = new AppointmentDto();
        appointmentDto2.setId(2L);
        appointmentDto2.setStartTime(LocalDateTime.of(2023, 1, 1, 11, 0));

        List<AppointmentDto> appointments = Arrays.asList(appointmentDto1, appointmentDto2);

        when(appointmentService.getPatientAppointments(1L)).thenReturn(appointments);

        mockMvc.perform(get("/appointments/patient/{patientId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getPatientAppointments_PatientNotFound_ThrowsException() throws Exception {
        when(appointmentService.getPatientAppointments(999L)).thenThrow(new PatientNotFoundException("Patient not found with id: 999"));

        mockMvc.perform(get("/appointments/patient/{patientId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void bookAppointment_CorrectData_AppointmentBooked() throws Exception {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setStartTime(LocalDateTime.now());

        when(appointmentService.bookAppointment(any(AppointmentDto.class), eq(1L)))
                .thenReturn(appointmentDto);

        mockMvc.perform(post("/appointments/book")
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentDto.getId()));
    }

    @Test
    void bookAppointment_InvalidData_ThrowsException() throws Exception {
        AppointmentDto appointmentDto = new AppointmentDto();

        when(appointmentService.bookAppointment(any(AppointmentDto.class), eq(1L)))
                .thenThrow(new InvalidAppointmentDetailsException());

        mockMvc.perform(post("/appointments/book")
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableAppointmentsForDoctor_CorrectData_ReturnAppointments() throws Exception {
        AppointmentDto appointmentDto1 = new AppointmentDto();
        appointmentDto1.setId(1L);
        appointmentDto1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));

        List<AppointmentDto> appointments = Collections.singletonList(appointmentDto1);
        PageRequest pageable = PageRequest.of(0, 10);

        when(appointmentService.getAvailableAppointmentsForDoctor(1L, pageable)).thenReturn(appointments);

        mockMvc.perform(get("/appointments/doctor/{doctorId}/available", 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAvailableAppointmentsForDoctor_DoctorNotFound_ThrowsException() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);

        when(appointmentService.getAvailableAppointmentsForDoctor(999L, pageable))
                .thenThrow(new DoctorNotFoundException("Doctor not found"));

        mockMvc.perform(get("/appointments/doctor/{doctorId}/available", 999L)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailableAppointmentsForSpecializationAndDay_CorrectData_ReturnAppointments() throws Exception {
        AppointmentDto appointmentDto1 = new AppointmentDto();
        appointmentDto1.setId(1L);
        appointmentDto1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));

        List<AppointmentDto> appointments = Collections.singletonList(appointmentDto1);
        PageRequest pageable = PageRequest.of(0, 10);

        when(appointmentService.getAvailableAppointmentsForSpecializationAndDay("Cardiology", LocalDate.of(2023, 1, 1), pageable))
                .thenReturn(appointments);

        mockMvc.perform(get("/appointments/specialization/{specialization}/date/{date}", "Cardiology", "2023-01-01")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void assignDoctorToAppointment_Success() throws Exception {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        doNothing().when(appointmentService).assignDoctorToAppointment(appointmentId, doctorId);

        mockMvc.perform(patch("/appointments/{appointmentId}/doctors/{doctorId}", appointmentId, doctorId))
                .andExpect(status().isOk());
    }

    @Test
    void assignDoctorToAppointment_AppointmentNotFound_ThrowsAppointmentNotFoundException() throws Exception {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        doThrow(new AppointmentNotFoundException("Appointment not found")).when(appointmentService).assignDoctorToAppointment(appointmentId, doctorId);

        mockMvc.perform(patch("/appointments/{appointmentId}/doctors/{doctorId}", appointmentId, doctorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignDoctorToAppointment_DoctorNotFound_ThrowsDoctorNotFoundException() throws Exception {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        doThrow(new DoctorNotFoundException("Doctor not found")).when(appointmentService).assignDoctorToAppointment(appointmentId, doctorId);

        mockMvc.perform(patch("/appointments/{appointmentId}/doctors/{doctorId}", appointmentId, doctorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignDoctorToAppointment_InternalServerError_ThrowsRuntimeException() throws Exception {
        Long appointmentId = 1L;
        Long doctorId = 1L;

        doThrow(new RuntimeException("Internal server error")).when(appointmentService).assignDoctorToAppointment(appointmentId, doctorId);

        mockMvc.perform(patch("/appointments/{appointmentId}/doctors/{doctorId}", appointmentId, doctorId))
                .andExpect(status().isInternalServerError());
    }

    private AppointmentDto createAppointmentDto(Long id, LocalDateTime startTime) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(id);
        appointmentDto.setStartTime(startTime);
        appointmentDto.setEndTime(startTime.plusHours(1));
        return appointmentDto;
    }
}