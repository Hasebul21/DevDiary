package com.example.devdiary.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.devdiary.dto.UserDto;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.security.JwtFilter;
import com.example.devdiary.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean private UserService mockUserService;
    @Autowired private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired private JwtFilter jwtFilter;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(jwtFilter, "/*").build();
    }

    @Test
    @DisplayName("GET/User Success")
    void getAllStory() throws Exception {
        mockMvc.perform(get("/api/v1/users/")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET/users/1   Found")
    void getSingleUserSuccess() throws Exception {
        UserDto mockUser = new UserDto(1, "haseb@gmail.com", "Haseb", "01789533586");
        when(mockUserService.getSingleUser(1)).thenReturn(mockUser);

        mockMvc.perform(get("/api/v1/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("haseb@gmail.com"))
                .andExpect(jsonPath("$.name").value("Haseb"))
                .andExpect(jsonPath("$.phone").value("01789533586"));
    }

    @Test
    @DisplayName("GET/users/1   NOT FOUND")
    void getSingleUserFailed() throws Exception {
        when(mockUserService.getSingleUser(1)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/users/{id}", 1)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT/users/1   SUCESSFUL")
    void updateUserSuccess() throws Exception {
        Users user = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        Users mockUser = new Users(1, "haseb@gmail.com", "abcdef", "Asif", "01789533586");
        UserDto mockUserDto = new UserDto(1, "haseb@gmail.com", "Asif", "01789533586");
        when(mockUserService.updateUser(1, user)).thenReturn(mockUserDto);

        mockMvc.perform(
                        put("/api/v1/users/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("haseb@gmail.com"))
                .andExpect(jsonPath("$.name").value("Asif"))
                .andExpect(jsonPath("$.phone").value("01789533586"));
    }

    @Test
    @DisplayName("PUT/users/1   Failed")
    void updateUserFailed() throws Exception {
        Users user = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        Users mockUser = new Users(1, "haseb@gmail.com", "abcdef", "Asif", "01789533586");
        when(mockUserService.updateUser(1, user)).thenThrow(AccessDeniedException.class);

        mockMvc.perform(
                        put("/api/v1/users/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(asJsonString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("DELETE/users/1  SUCCESS")
    void deleteUserSuccess() throws Exception {
        doNothing().when(mockUserService).deleteUser(1);

        mockMvc.perform(delete("/api/v1/users/{id}", 1)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE/users/1  Failed")
    void deleteUserFailed() throws Exception {
        doThrow(EntityNotFoundException.class).when(mockUserService).deleteUser(1);
        mockMvc.perform(delete("/api/v1/users/{id}", 1)).andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
