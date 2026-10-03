package com.example.devdiary.controller;

import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.DuplicateEmailException;
import com.example.devdiary.repository.UserRepository;
import com.example.devdiary.security.Authenticate;
import com.example.devdiary.security.JwtFilter;
import com.example.devdiary.service.AuthService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @MockBean
    private Authenticate authenticate;

    @MockBean
    private UserRepository mockuserRepository;
    @MockBean
    private AuthService authService;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private JwtFilter jwtFilter;

    @BeforeEach
    public void setup(){
        mockMvc= MockMvcBuilders
                .webAppContextSetup(context).addFilter(jwtFilter, "/*").build();
    }

    @Test
    @DisplayName("POST/signup  Success")
    void createUserSuccess() throws Exception{

        Users user=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(asJsonString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST/signup  Failed")
    void createUserFailed() throws Exception{

        Users user=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        doThrow(DuplicateEmailException.class).when(authService).signUp(user);
        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(asJsonString(user)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("POST/signin Success")
    void loginUserScuess() throws Exception{

        Users user=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        when(authService.signIn(user)).thenReturn(user);
        mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(asJsonString(user)))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
