package com.wojteknier03.clinic_medical.controller;

import com.wojteknier03.clinic_medical.dto.UserDto;
import com.wojteknier03.clinic_medical.model.AppUser;
import com.wojteknier03.clinic_medical.repository.UserRepository;
import com.wojteknier03.clinic_medical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping
    public List<UserDto> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        return userService.updatePassword(id, newPassword);
    }
}