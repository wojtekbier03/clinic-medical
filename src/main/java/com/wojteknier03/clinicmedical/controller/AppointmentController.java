package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Add a new appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment successfully added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid appointment details provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public AppointmentDto addAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.addAppointment(appointmentDto);
    }

    @Operation(summary = "Get a list of appointments with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<AppointmentDto> getAppointments(@Parameter(description = "Pagination information") Pageable pageable) {
        return appointmentService.getAppointments(pageable);
    }

    @Operation(summary = "Assign a patient to an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully assigned to appointment",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Appointment or patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("{appointmentId}/patients/{patientId}")
    public void assignPatientToAppointment(
            @Parameter(description = "ID of the appointment to which the patient will be assigned") @PathVariable Long appointmentId,
            @Parameter(description = "ID of the patient to be assigned to the appointment") @PathVariable Long patientId) {
        appointmentService.assignPatientToAppointment(appointmentId, patientId);
    }
}