package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.exceptions.patientEx.PatientNotFoundException;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import com.wojteknier03.clinicmedical.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void getPatientByEmail_CorrectData_ReturnPatient() throws Exception {
        PatientDto patientDto = PatientDto.builder()
                .id(1L)
                .email("email")
                .idCardNo("123")
                .firstName("Test")
                .lastName("Name")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(1990, 1, 1))
                .userId(1L)
                .build();

        when(patientService.getPatientByEmail("email")).thenReturn(patientDto);

        mockMvc.perform(get("/patients/{email}", "email"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("email"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Name"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("1990-01-01"))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    public void getPatientByEmail_PatientNotFound_ReturnsNotFound() throws Exception {
        when(patientService.getPatientByEmail("email")).thenReturn(null);

        mockMvc.perform(get("/patients/{email}", "email"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void addPatient_CorrectData_ReturnAddedPatient() throws Exception {
        PatientDto patientDto = PatientDto.builder()
                .id(1L)
                .email("email")
                .idCardNo("123")
                .firstName("New")
                .lastName("Name")
                .phoneNumber("987654321")
                .birthday(LocalDate.of(1995, 5, 15))
                .userId(2L)
                .build();

        when(patientService.add(any(PatientDto.class))).thenReturn(patientDto);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("email"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("Name"))
                .andExpect(jsonPath("$.phoneNumber").value("987654321"))
                .andExpect(jsonPath("$.birthday").value("1995-05-15"))
                .andExpect(jsonPath("$.userId").value(2L));
    }

    @Test
    public void deletePatient_CorrectData_StatusNoContent() throws Exception {
        doNothing().when(patientService).delete("test@example.com");

        mockMvc.perform(delete("/patients/{email}", "test@example.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deletePatient_PatientNotFound_ReturnsNotFound() throws Exception {
        doThrow(new PatientNotFoundException("Patient not found")).when(patientService).delete("nonexistent@example.com");

        mockMvc.perform(delete("/patients/{email}", "nonexistent@example.com"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePatientByEmail_CorrectData_ReturnUpdatedPatient() throws Exception {
        PatientDto updatedPatientDto = PatientDto.builder()
                .id(1L)
                .email("updated@")
                .idCardNo("123456")
                .firstName("Updated")
                .lastName("Name")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(1990, 1, 1))
                .userId(1L)
                .build();

        when(patientService.update(eq("test@example.com"), any(PatientDto.class))).thenReturn(updatedPatientDto);

        mockMvc.perform(put("/patients/{email}", "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatientDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("updated@"))
                .andExpect(jsonPath("$.idCardNo").value("123456"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("1990-01-01"))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    public void updatePatientByEmail_PatientNotFound_ReturnsNotFound() throws Exception {
        when(patientService.update(eq("nonexistent@example.com"), any(PatientDto.class))).thenReturn(null);

        PatientDto updatedPatientDto = PatientDto.builder()
                .id(1L)
                .email("updated@")
                .idCardNo("123456")
                .firstName("Updated")
                .lastName("Name")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(1990, 1, 1))
                .userId(1L)
                .build();

        mockMvc.perform(put("/patients/{email}", "nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatientDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
