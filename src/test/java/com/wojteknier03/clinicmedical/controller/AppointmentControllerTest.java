package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.exceptions.InvalidPaginationParametersException;
import com.wojteknier03.clinicmedical.exceptions.appointmentEx.InvalidAppointmentDetailsException;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

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

//    @Test
//    void assignPatientToAppointment_PatientNotFound_ThrowsException() throws Exception {
//        Long appointmentId = 1L;
//        Long invalidPatientId = 999L;
//
//        Mockito.doThrow(new IllegalArgumentException("Patient not found"))
//                .when(appointmentService).assignPatientToAppointment(appointmentId, invalidPatientId);
//
//        mockMvc.perform(patch("/appointments/{appointmentId}/patients/{patientId}", appointmentId, invalidPatientId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Patient not found"));
//    }
}