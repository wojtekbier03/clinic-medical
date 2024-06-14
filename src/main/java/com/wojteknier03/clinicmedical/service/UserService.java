package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.UserDto;
import com.wojteknier03.clinicmedical.mapper.UserMapper;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // 1. Sprawdzenie, czy użytkownik o podanej nazwie już istnieje (userRepository.findByUsername)
    // 2. Jeśli użytkownik istnieje(userRepository.findByUsername(userDto.getUsername()) zwraca Optionala z userem), rzucamy wyjątek
    // 3. Zamiana UserDto na AppUser za pomocą metody userDtoToUser z userMapper
    // 4. Zapisanie użytkownika w userRepository (userRepository.save(user))
    // 5. Zamiana zapisanego użytkownika na UserDto za pomocą metody userToUserDto z userMapper
    @Transactional
    public UserDto addUser(UserDto userDto) {
        Optional<AppUser> existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        AppUser user = userMapper.userDtoToUser(userDto);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    // 1. Pobranie listy użytkowników (userRepository.findAll(pageable).getContent)
    // 2. Zamiana listy użytkowników na listę UserDto za pomocą metody toDtoList z userMapper
    public List<UserDto> getUsers(Pageable pageable) {
        List<AppUser> users = userRepository.findAll(pageable).getContent();
        return userMapper.toDtoList(users);
    }

    // 1. Pobranie użytkownika za pomocą identyfikatora (userRepository.findById)
    // 2. Jeśli użytkownik nie istnieje (userRepository.findById(id) zwraca Optional.empty), rzucamy wyjątek
    // 3. Zwracamy znalezionego użytkownika
    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // 1. Pobranie użytkownika za pomocą identyfikatora (userRepository.findById)
    // 2. Jeśli użytkownik nie istnieje(userRepository.findById zwraca Optional.empty), rzucamy wyjątek
    // 3. Aktualizacja hasła użytkownika (user.setPassword(newPassword))
    // 4. Zapisanie zaktualizowanego użytkownika w userRepository (userRepository.save(user))
    // 5. Zwracamy nowe hasło
    @Transactional
    public String updatePassword(Long id, String newPassword){
        AppUser user= userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(newPassword);
        userRepository.save(user);
        return newPassword;
    }
}