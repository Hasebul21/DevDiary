package com.example.devdiary.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.devdiary.repository.UserRepository;
import com.example.devdiary.security.Authenticate;
import com.example.devdiary.service.AuthService;
import com.example.devdiary.service.CommentService;
import com.example.devdiary.service.LikeService;
import com.example.devdiary.service.StoryService;
import com.example.devdiary.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class GuestAccessTest {

    @MockBean private UserRepository userRepository;
    @MockBean private StoryService storyService;
    @MockBean private CommentService commentService;
    @MockBean private LikeService likeService;
    @MockBean private UserService userService;
    @MockBean private AuthService authService;
    @MockBean private Authenticate authenticate;
    @Autowired private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(roles = "GUEST")
    @DisplayName("Guest can read stories")
    void guestCanRead() throws Exception {
        mockMvc.perform(get("/api/v1/stories/")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/stories/my")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "GUEST")
    @DisplayName("Guest can not create, update or delete")
    void guestCanNotWrite() throws Exception {
        mockMvc.perform(
                        post("/api/v1/stories/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"title\":\"t\",\"description\":\"d\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(
                        put("/api/v1/stories/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"title\":\"t\",\"description\":\"d\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/stories/{id}", 1)).andExpect(status().isForbidden());
        mockMvc.perform(
                        post("/api/v1/stories/{id}/comments", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"text\":\"hi\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/comments/{id}", 1)).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/stories/{id}/likes", 1)).andExpect(status().isForbidden());
        mockMvc.perform(
                        put("/api/v1/users/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"name\":\"x\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/users/{id}", 1)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("User can still create a story")
    void userCanWrite() throws Exception {
        mockMvc.perform(
                        post("/api/v1/stories/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"title\":\"t\",\"description\":\"d\"}"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Anyone can still sign in as guest")
    void guestSignInIsOpen() throws Exception {
        mockMvc.perform(post("/api/v1/signin/guest")).andExpect(status().isOk());
    }
}
