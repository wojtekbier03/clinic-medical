package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.DoctorDto;
import com.wojteknier03.clinicmedical.exceptions.InvalidPaginationParametersException;
import com.wojteknier03.clinicmedical.exceptions.doctorEx.DoctorNotFoundException;
import com.wojteknier03.clinicmedical.exceptions.doctorEx.InvalidDoctorDetailsException;
import com.wojteknier03.clinicmedical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

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
        if (doctorDto == null || doctorDto.getLastName() == null || doctorDto.getLastName().isEmpty()) {
            throw new InvalidDoctorDetailsException("Invalid doctor details provided");
        }
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
    public List<DoctorDto> getAllDoctors(Pageable pageable) {
        if (pageable == null) {
            throw new InvalidPaginationParametersException();
        }
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
    public DoctorDto getDoctorById(@PathVariable Long id) {
        DoctorDto doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            throw new DoctorNotFoundException("Doctor not found");
        }
        return doctor;
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
    public void deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
        } catch (DoctorNotFoundException ex) {
            throw new DoctorNotFoundException("Doctor not found");
        }
    }
}