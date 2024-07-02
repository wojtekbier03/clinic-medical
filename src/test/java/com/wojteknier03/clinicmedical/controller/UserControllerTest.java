package com.wojteknier03.clinicmedical.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wojteknier03.clinicmedical.dto.UserDto;
import com.wojteknier03.clinicmedical.model.AppUser;
import com.wojteknier03.clinicmedical.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void addUser_CorrectData_UserAdded() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("name");
        userDto.setPassword("password");

        when(userService.addUser(any(UserDto.class)));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    public void addUser_MissingData_ReturnsBadRequest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername(""); // Missing password

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsers_ReturnListOfUsers() throws Exception{
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(1L, "user", "password"));

        when(userService.getUsers(any())).thenReturn(userDtoList);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user"));
    }



    @Test
    public void getUserById_ReturnsUserById() throws Exception{
        Long id = 1L;
        AppUser user = new AppUser();
        user.setId(id);
        user.setUsername("name");

        when(userService.getUserById(id)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$id").value(id))
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    public void getUserById_UserNotFound_ReturnsNotFound() throws Exception {
        Long id = 1L;

        when(userService.getUserById(id)).thenReturn(null);

        mockMvc.perform(get("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePassword_CorrectData_PasswordUpdated() throws Exception{
        Long id = 1L;
        String newPassword = "password";

        when(userService.updatePassword(id, newPassword));

        mockMvc.perform(patch("/users/{id}/password", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }

    @Test
    public void updatePassword_UserNotFound_ReturnsNotFound() throws Exception {
        Long id = 1L;
        String newPassword = "password";

        when(userService.updatePassword(anyLong(), any(String.class))).thenReturn(String.valueOf(false));

        mockMvc.perform(patch("/users/{id}/password", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isNotFound());
    }

}
