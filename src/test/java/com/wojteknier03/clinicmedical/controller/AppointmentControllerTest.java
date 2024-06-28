package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void addAppointment_CorrectData_AppointmentSaved() throws Exception{
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);

        Mockito.when(appointmentService.addAppointment(any(AppointmentDto.class)))
                .thenReturn(appointmentDto);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentDto.getId()));
    }

    @Test
    void getAppointments_ReturnsListOfAppointments() throws Exception{
        AppointmentDto appointmentDto = new AppointmentDto();

        Mockito.when(appointmentService.getAppointments(any()))
                .thenReturn(List.of(appointmentDto));

        mockMvc.perform(get("/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(appointmentDto.getId()));
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
    void assignPatientToAppointment_NonExistentAppointmentId_ReturnNotFound() throws Exception {
        Long appointmentId = 999L;
        Long patientId = 1L;

        Mockito.doThrow(new RuntimeException("Appointment not found"))
                .when(appointmentService).assignPatientToAppointment(anyLong(), anyLong());

        mockMvc.perform(patch("/appointments/{appointmentId}/patients/{patientId}", appointmentId, patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignPatientToAppointment_NonExistentPatientId_ReturnNotFound() throws Exception {
        Long appointmentId = 1L;
        Long patientId = 999L;

        Mockito.doThrow(new RuntimeException("Patient not found"))
                .when(appointmentService).assignPatientToAppointment(anyLong(), anyLong());

        mockMvc.perform(patch("/appointments/{appointmentId}/patients/{patientId}", appointmentId, patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
