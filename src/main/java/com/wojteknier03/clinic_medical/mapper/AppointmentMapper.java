package com.wojteknier03.clinic_medical.mapper;


import com.wojteknier03.clinic_medical.dto.AppointmentDto;
import com.wojteknier03.clinic_medical.model.Appointment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDto toDto(Appointment appointment);
    Appointment fromDto(AppointmentDto appointmentDto);

    default List<AppointmentDto> toDtoList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .toList();
    }
}