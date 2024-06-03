package com.wojteknier03.clinic_medical.controller;

import com.wojteknier03.clinic_medical.dto.UserDto;
import com.wojteknier03.clinic_medical.model.User;
import com.wojteknier03.clinic_medical.repository.UserRepository;
import com.wojteknier03.clinic_medical.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public List<com.wojteknier03.clinic_medical.model.User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        return userService.updatePassword(id, newPassword);
    }
}
