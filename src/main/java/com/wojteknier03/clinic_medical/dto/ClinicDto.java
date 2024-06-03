package com.wojteknier03.clinic_medical.dto;

import lombok.Data;

@Data
public class ClinicDto {
    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNumber;
}
