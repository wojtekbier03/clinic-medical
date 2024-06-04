package com.wojteknier03.clinic_medical.dto;

import lombok.Data;

import java.util.Set;

@Data
public class DoctorDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
    private Set<Long> clinicIds;
    private Set<ClinicDto> clinics;
}
