package com.example.techblogapi.controller;

import com.example.techblogapi.dto.CommentDto;
import com.example.techblogapi.dto.LikeDto;
import com.example.techblogapi.entity.Comments;
import com.example.techblogapi.exception.AccessDeniedException;
import com.example.techblogapi.exception.EntityNotFoundException;
import com.example.techblogapi.security.JwtFilter;
import com.example.techblogapi.service.CommentService;
import com.example.techblogapi.service.LikeService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentLikeControllerTest {

    @MockBean
    private CommentService mockCommentService;

    @MockBean
    private LikeService mockLikeService;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private JwtFilter jwtFilter;

    @BeforeEach
    public void setup(){
        mockMvc= MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(jwtFilter, "/*")
                .build();
    }

    @Test
    @DisplayName("GET/stories/1/comments  Success")
    void getCommentOfStory() throws Exception{

        CommentDto commentDto=new CommentDto(1,"Nice post","haseb@gmail.com",1);
        when(mockCommentService.getCommentOfStory(1)).thenReturn(Arrays.asList(commentDto));

        mockMvc.perform(get("/api/v1/stories/{id}/comments",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Nice post"))
                .andExpect(jsonPath("$[0].author").value("haseb@gmail.com"));
    }

    @Test
    @DisplayName("GET/stories/1/comments  NOT FOUND")
    void getCommentOfStoryFailed() throws Exception{

        when(mockCommentService.getCommentOfStory(1)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/stories/{id}/comments",1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST/stories/1/comments  Success")
    void addComment() throws Exception{

        CommentDto commentDto=new CommentDto(1,"Nice post","haseb@gmail.com",1);
        when(mockCommentService.addComment(eq(1),any(Comments.class))).thenReturn(commentDto);

        mockMvc.perform(post("/api/v1/stories/{id}/comments",1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"text\":\"Nice post\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Nice post"));
    }

    @Test
    @DisplayName("DELETE/comments/1  FAILED")
    void deleteCommentFailed() throws Exception{

        doThrow(AccessDeniedException.class).when(mockCommentService).deleteComment(1);

        mockMvc.perform(delete("/api/v1/comments/{id}",1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST/stories/1/likes  Success")
    void toggleLike() throws Exception{

        when(mockLikeService.toggleLike(1)).thenReturn(new LikeDto(1,Arrays.asList("haseb@gmail.com")));

        mockMvc.perform(post("/api/v1/stories/{id}/likes",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.users[0]").value("haseb@gmail.com"));
    }
}
