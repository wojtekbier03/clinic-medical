package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojteknier03.clinicmedical.dto.ClinicDto;
import com.wojteknier03.clinicmedical.service.ClinicService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClinicService clinicService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addClinic_CorrectData_ClinicSaved() throws Exception {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setId(1L);

        Mockito.when(clinicService.addClinic(any(ClinicDto.class)))
                .thenReturn(clinicDto);

        mockMvc.perform(post("/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clinicDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clinicDto.getId()));
    }

    @Test
    void getAllClinics_ReturnsListOfClinics() throws Exception {
        ClinicDto clinicDto = new ClinicDto();
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(clinicService.getClinics(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(clinicDto)).getContent());

        mockMvc.perform(get("/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(clinicDto.getId()))
                .andDo(print());
    }

    @Test
    void getClinicById_ReturnsClinic() throws Exception {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setId(1L);

        Mockito.when(clinicService.getClinicById(anyLong()))
                .thenReturn(clinicDto);

        mockMvc.perform(get("/clinics/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clinicDto.getId()));
    }

    @Test
    void deleteClinic_CorrectId_ClinicDeleted() throws Exception {
        mockMvc.perform(delete("/clinics/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(clinicService, Mockito.times(1))
                .deleteClinic(1L);
    }
}