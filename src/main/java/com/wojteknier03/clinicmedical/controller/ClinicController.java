package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.ClinicDto;
import com.wojteknier03.clinicmedical.exceptions.InvalidPaginationParametersException;
import com.wojteknier03.clinicmedical.exceptions.clinicEx.ClinicNotFoundException;
import com.wojteknier03.clinicmedical.exceptions.clinicEx.InvalidClinicDetailsException;
import com.wojteknier03.clinicmedical.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @Operation(summary = "Add a new clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinic successfully added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid clinic details provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ClinicDto addClinic(@Valid @RequestBody ClinicDto clinicDto) {
        if (clinicDto.getName() == null || clinicDto.getName().isEmpty()) {
            throw new InvalidClinicDetailsException();
        }
        return clinicService.addClinic(clinicDto);
    }

    @Operation(summary = "Get a list of all clinics with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clinics retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<ClinicDto> getAllClinics(@Parameter(description = "Pagination information") Pageable pageable) {
        if (pageable == null) {
            throw new InvalidPaginationParametersException();
        }
        return clinicService.getClinics(pageable);
    }

    @Operation(summary = "Get clinic details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinic details retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Clinic not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ClinicDto getClinicById(@Parameter(description = "ID of the clinic to retrieve") @PathVariable Long id) {
        ClinicDto clinicDto = clinicService.getClinicById(id);
        if (clinicDto == null) {
            throw new ClinicNotFoundException();
        }
        return clinicDto;
    }

    @Operation(summary = "Delete clinic by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinic successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Clinic not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClinic(@Parameter(description = "ID kliniki do usunięcia") @PathVariable Long id) {
        ClinicDto clinicDto = clinicService.getClinicById(id);
        if (clinicDto == null) {
            throw new ClinicNotFoundException();
        }
        clinicService.deleteClinic(id);
    }
}