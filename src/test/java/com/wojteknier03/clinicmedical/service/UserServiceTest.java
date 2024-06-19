package com.wojteknier03.clinicmedical.service;

import com.wojteknier03.clinicmedical.dto.UserDto;
import com.wojteknier03.clinicmedical.mapper.UserMapper;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userService = new UserService(userRepository, userMapper);
    }

    @Test
   void addUser_User_ValidUser_UserAdded(){
        //given
        UserDto userDto = new UserDto();
        userDto.setUsername("user");
        userDto.setId(3L);

        AppUser user = createUser("user", 3L);

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        //when
        UserDto result = userService.addUser(userDto);

        //then
        Assertions.assertEquals("newUser", result.getUsername());
        Assertions.assertEquals(3L, result.getId());
    }

    @Test
    void addUser_UsernameAlreadyExists_ExceptionThrown() {
        //given
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUser");

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new AppUser()));

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(userDto);
        });
    }

    @Test
    void getUsers_UsersExist_UsersReturned(){
        //given
        List<AppUser> users = new ArrayList<>();
        users.add(createUser("user1", 1L));
        users.add(createUser("user2", 2L));

        when(userRepository.findAll()).thenReturn(users);

        //when
        List<UserDto> result = userService.getUsers(Pageable.unpaged());

        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals("user1", result.get(0).getUsername());
        Assertions.assertEquals(2L, result.get(1).getId());
        Assertions.assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    void getUserById_UserExists_UserReturned() {
        //given
        AppUser user = createUser("user1", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //when
        AppUser result = userService.getUserById(user.getId());

        //then
        Assertions.assertEquals(1L,result.getId());
        Assertions.assertEquals("user1", result.getUsername());
    }

    @Test
    void getUserById_UserNotFound_ExceptionThrown() {
        //given
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(userId);
        });
    }

    @Test
    void updatePassword_CorrectData_PasswordUpdated(){
        //given
        Long userId = 1L;
        String newPassword = "newPassword";
        AppUser user = new AppUser();
        user.setId(userId);
        user.setUsername("user1");
        user.setPassword("oldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        //when
        String result = userService.updatePassword(userId, newPassword);

        //then
        Assertions.assertEquals(newPassword, result);
        Assertions.assertEquals(newPassword, user.getPassword());
    }

    @Test
    void updatePassword_UserNotFound_ExceptionThrown() {
        //given
        Long userId = 999L;
        String newPassword = "newPassword";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword(userId, newPassword);
        });
    }

    AppUser createUser(String username, Long id){
        AppUser user = new AppUser();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
}
