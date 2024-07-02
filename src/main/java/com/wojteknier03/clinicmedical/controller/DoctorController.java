package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.service.DoctorService;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Add a new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor successfully added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid doctor details provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public DoctorDto addDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.addDoctor(doctorDto);
    }

    @Operation(summary = "Get a list of all doctors with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of doctors retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<DoctorDto> getAllDoctors(@Parameter(description = "Pagination information") Pageable pageable){
        return doctorService.getAllDoctors(pageable);
    }

    @Operation(summary = "Get doctor details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor details retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@Parameter(description = "ID of the doctor to retrieve") @PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @Operation(summary = "Delete doctor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteDoctor(@Parameter(description = "ID of the doctor to delete") @PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @Operation(summary = "Assign a doctor to a clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor successfully assigned to clinic",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Doctor or clinic not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/{doctorId}/clinics/{clinicId}")
    public void assignDoctorToClinic(
            @Parameter(description = "ID of the doctor to assign") @PathVariable Long doctorId,
            @Parameter(description = "ID of the clinic to assign the doctor to") @PathVariable Long clinicId) {
        doctorService.assignDoctor(doctorId, clinicId);
    }
}
