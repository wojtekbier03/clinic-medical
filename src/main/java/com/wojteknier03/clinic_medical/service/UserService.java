package com.wojteknier03.clinic_medical.service;

import com.wojteknier03.clinic_medical.dto.UserDto;
import com.wojteknier03.clinic_medical.mapper.PatientMapper;
import com.wojteknier03.clinic_medical.model.AppUser;
import com.wojteknier03.clinic_medical.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public UserDto addUser(UserDto userDto) {
        Optional<AppUser> existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        AppUser user = patientMapper.userDtoToUser(userDto);
        return patientMapper.userToUserDto(userRepository.save(user));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(patientMapper::userToUserDto)
                .toList();
    }

    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public String updatePassword(Long id, String newPassword){
        AppUser user= userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(newPassword);
        userRepository.save(user);
        return newPassword;
    }
}
