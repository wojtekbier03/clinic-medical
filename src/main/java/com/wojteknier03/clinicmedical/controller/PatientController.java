package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import com.wojteknier03.clinicmedical.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Operation(summary = "Get a patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient details retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@Parameter(description = "Email of the patient to retrieve") @PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @Operation(summary = "Add a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient successfully added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid patient details provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public PatientDto add(@RequestBody PatientDto patientDto) {
        return patientService.add(patientDto);
    }

    @Operation(summary = "Delete a patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "Email of the patient to delete") @PathVariable String email) {
        patientService.delete(email);
    }

    @Operation(summary = "Update a patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid patient details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{email}")
    public PatientDto updatePatientByEmail(@Parameter(description = "Email of the patient to update") @PathVariable String email, @RequestBody PatientDto updatedPatientDto) {
        return patientService.update(email, updatedPatientDto);
    }

    @Operation(summary = "Get appointments for a patient by patient ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{patientId}/appointments")
    public List<AppointmentDto> getAppointmentsByPatientId(@Parameter(description = "ID of the patient to retrieve appointments for") @PathVariable Long patientId) {
        return appointmentService.getAppointmentByPatientId(patientId);
    }
}
