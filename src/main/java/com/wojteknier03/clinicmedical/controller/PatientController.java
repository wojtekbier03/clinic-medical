package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.PatientDto;
import com.wojteknier03.clinicmedical.exceptions.patientEx.InvalidPatientDetailsException;
import com.wojteknier03.clinicmedical.exceptions.patientEx.PatientNotFoundException;
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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PatientDto> getPatientByEmail(
            @Parameter(description = "Email of the patient to retrieve") @PathVariable String email) {
        try {
            PatientDto patient = patientService.getPatientByEmail(email);
            if (patient == null) {
                throw new PatientNotFoundException("Patient not found with email: " + email);
            }
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
        if (patientDto == null || patientDto.getLastName() == null || patientDto.getLastName().isEmpty()) {
            throw new InvalidPatientDetailsException("Invalid patient details provided");
        }
        return patientService.add(patientDto);
    }

    @Operation(summary = "Delete a patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{email}")
    public void delete(@PathVariable String email) {
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
    public PatientDto update(@Parameter(description = "Email of the patient to update") @PathVariable String email, @RequestBody PatientDto updatedPatientDto) {
        PatientDto updatedPatient = patientService.update(email, updatedPatientDto);
        if (updatedPatient == null) {
            throw new PatientNotFoundException("Patient not found");
        }
        return updatedPatient;
    }
}
