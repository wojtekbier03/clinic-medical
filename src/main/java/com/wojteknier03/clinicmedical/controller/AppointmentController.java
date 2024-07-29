package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.AppointmentDto;
import com.wojteknier03.clinicmedical.exceptions.appointmentEx.InvalidAppointmentDetailsException;
import com.wojteknier03.clinicmedical.exceptions.InvalidPaginationParametersException;
import com.wojteknier03.clinicmedical.exceptions.patientEx.PatientNotFoundException;
import com.wojteknier03.clinicmedical.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        if (appointmentDto == null || appointmentDto.getStartTime() == null) {
            throw new InvalidAppointmentDetailsException();
        }
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
        if (pageable == null) {
            throw new InvalidPaginationParametersException();
        }
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
        try {
            return appointmentService.getAppointmentByPatientId(patientId);
        } catch (PatientNotFoundException ex) {
            throw new PatientNotFoundException("Patient not found with id: " + patientId);
        }
    }

    @Operation(summary = "Get all appointments for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of appointments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/patient/{patientId}")
    public List<AppointmentDto> getPatientAppointments(@Parameter(description = "ID of the patient to retrieve appointments for") @PathVariable Long patientId) {
        try {
            return appointmentService.getPatientAppointments(patientId);
        } catch (PatientNotFoundException ex) {
            throw new PatientNotFoundException("Patient not found with id: " + patientId);
        }
    }

    @Operation(summary = "Book an appointment for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment successfully booked",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid appointment details or patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/book")
    public AppointmentDto bookAppointment(
            @RequestBody AppointmentDto appointmentDto,
            @Parameter(description = "ID of the patient booking the appointment") @RequestParam Long patientId) {
        if (appointmentDto == null || appointmentDto.getStartTime() == null) {
            throw new InvalidAppointmentDetailsException();
        }
        return appointmentService.bookAppointment(appointmentDto, patientId);
    }

    @Operation(summary = "Get available appointments for a specific doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available appointments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/doctor/{doctorId}/available")
    public List<AppointmentDto> getAvailableAppointmentsForDoctor(
            @Parameter(description = "ID of the doctor to retrieve available appointments for") @PathVariable Long doctorId,
            @Parameter(description = "Pagination information") Pageable pageable) {
        return appointmentService.getAvailableAppointmentsForDoctor(doctorId, pageable);
    }

    @Operation(summary = "Get available appointments for a specific specialization and day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available appointments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid date or specialization",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/specialization/{specialization}/date/{date}")
    public List<AppointmentDto> getAvailableAppointmentsForSpecializationAndDay(
            @Parameter(description = "Specialization of the doctors") @PathVariable String specialization,
            @Parameter(description = "Date for which to retrieve available appointments (yyyy-MM-dd)") @PathVariable String date,
            @Parameter(description = "Pagination information") Pageable pageable) {
        LocalDate appointmentDate = LocalDate.parse(date);
        return appointmentService.getAvailableAppointmentsForSpecializationAndDay(specialization, appointmentDate, pageable);
    }

    @Operation(summary = "Assign a doctor to an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor successfully assigned to appointment",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Appointment or doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("{appointmentId}/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    public void assignDoctorToAppointment(
            @Parameter(description = "ID of the appointment to which the doctor will be assigned") @PathVariable Long appointmentId,
            @Parameter(description = "ID of the doctor to be assigned to the appointment") @PathVariable Long doctorId) {
        appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
    }
}