package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.exceptions.doctorEx.DoctorNotFoundException;
import com.wojteknier03.clinicmedical.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String path = "/mypath/schema";
    private static final String jsonPath = "$.myObject.val";
    private static final String defaultVal = "HELLO";

    @Test
    public void addDoctor_CorrectData_ReturnAddedDoctor() throws Exception {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(1L);
        doctorDto.setLastName("name");

        when(doctorService.addDoctor(any(DoctorDto.class))).thenReturn(doctorDto);

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lastName").value("name"));
    }

    @Test
    public void getAllDoctors_CorrectData_ReturnDoctorsList() throws Exception {
        DoctorDto doctorDto1 = new DoctorDto();
        doctorDto1.setId(1L);
        doctorDto1.setLastName("name");

        DoctorDto doctorDto2 = new DoctorDto();
        doctorDto2.setId(2L);
        doctorDto2.setLastName("name2");

        List<DoctorDto> doctors = Arrays.asList(doctorDto1, doctorDto2);
        Pageable pageable = Pageable.unpaged();

        when(doctorService.getAllDoctors(any(Pageable.class))).thenReturn(doctors);

        mockMvc.perform(get("/doctors")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].lastName").value("name"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].lastName").value("name2"));
    }

    @Test
    public void getDoctorById_CorrectData_ReturnDoctor() throws Exception {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(1L);
        doctorDto.setLastName("name");

        when(doctorService.getDoctorById(1L)).thenReturn(doctorDto);

        mockMvc.perform(get("/doctors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lastName").value("name"));
    }

    @Test

    public void deleteDoctor_CorrectData_StatusNoContent() throws Exception {
        mockMvc.perform(delete("/doctors/{id}", 1L))
                .andReturn();
    }

    @Test
    public void deleteDoctor_DoctorNotFound_ReturnNotFound() throws Exception {
        doThrow(new DoctorNotFoundException("Doctor not found")).when(doctorService).deleteDoctor(999L);

        mockMvc.perform(delete("/doctors/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
