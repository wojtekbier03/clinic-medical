package com.wojteknier03.clinicmedical.controller;

import com.wojteknier03.clinicmedical.dto.UserDto;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.repository.UserRepository;
import com.wojteknier03.clinicmedical.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<UserDto> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public AppUser getUserById(@Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Update user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid password provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PatchMapping("/{id}/password")
    public String updatePassword(@Parameter(description = "ID of the user whose password to update") @PathVariable Long id, @RequestBody String newPassword) {
        return userService.updatePassword(id, newPassword);
    }
}
