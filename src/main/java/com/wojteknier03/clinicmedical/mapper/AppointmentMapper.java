package com.wojteknier03.clinicmedical.mapper;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.model.Appointment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDto toDto(Appointment appointment);
    Appointment fromDto(AppointmentDto appointmentDto);
    List<AppointmentDto> toDtoList(List<Appointment> appointments);
}