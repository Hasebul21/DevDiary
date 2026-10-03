package com.example.devdiary.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.dto.StoryPageDto;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.security.JwtFilter;
import com.example.devdiary.service.StoryService;
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

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StoryControllerTest {

    @MockBean private StoryService mockStoryService;

    @Autowired private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired private JwtFilter jwtFilter;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(jwtFilter, "/*").build();
    }

    @Test
    @DisplayName("GET/stories Success")
    void getAllStory() throws Exception {
        mockMvc.perform(get("/api/v1/stories/")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET/stories/1   Found")
    void getSingleStorySuccess() throws Exception {
        StoryDto storyDto = new StoryDto(1, "haseb@gmail.com", "Spring", "Spring boot is a magic");
        when(mockStoryService.getSingleStory(1)).thenReturn(storyDto);

        mockMvc.perform(get("/api/v1/stories/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("haseb@gmail.com"))
                .andExpect(jsonPath("$.title").value("Spring"))
                .andExpect(jsonPath("$.description").value("Spring boot is a magic"));
    }

    @Test
    @DisplayName("GET/stories/1   NOT FOUND")
    void getSingleStoryFailed() throws Exception {
        when(mockStoryService.getSingleStory(1)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/stories/{id}", 1)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST/users/1   SUCESSFUL")
    void PostStorySuccess() throws Exception {
        Storys story = new Storys("Spring", "Spring boot is a magic");
        StoryDto mockstoryDto =
                new StoryDto(1, "haseb@gmail.com", "Spring", "Spring boot is a magic");
        when(mockStoryService.postStory(story)).thenReturn(mockstoryDto);

        mockMvc.perform(
                        post("/api/v1/stories/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(asJsonString(story)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("haseb@gmail.com"))
                .andExpect(jsonPath("$.title").value("Spring"))
                .andExpect(jsonPath("$.description").value("Spring boot is a magic"));
    }

    @Test
    @DisplayName("POST/users/1   FAILED")
    void PostStoryFailed() throws Exception {
        Storys story = new Storys("Spring boot", "Spring boot is a magic");
        StoryDto mockstoryDto =
                new StoryDto(1, "haseb@gmail.com", "Spring boot", "Spring boot is hard.Really!!!!");
        when(mockStoryService.postStory(story)).thenThrow(AccessDeniedException.class);

        mockMvc.perform(
                        post("/api/v1/stories/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(asJsonString(story)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("UPDATE/users/1   SUCESSFUL")
    void updateStorySuccess() throws Exception {
        Storys story = new Storys("Spring boot", "Spring boot is a magic");
        StoryDto mockstoryDto =
                new StoryDto(1, "haseb@gmail.com", "Spring boot", "Spring boot is hard.Really!!!!");
        when(mockStoryService.updateStory(1, story)).thenReturn(mockstoryDto);

        mockMvc.perform(
                        put("/api/v1/stories/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(asJsonString(story)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("haseb@gmail.com"))
                .andExpect(jsonPath("$.title").value("Spring boot"))
                .andExpect(jsonPath("$.description").value("Spring boot is hard.Really!!!!"));
    }

    @Test
    @DisplayName("UPDATE/users/1   FAILED")
    void updateStoryFailed() throws Exception {
        Storys story = new Storys("Spring boot", "Spring boot is a magic");
        StoryDto mockstoryDto =
                new StoryDto(1, "haseb@gmail.com", "Spring boot", "Spring boot is hard.Really!!!!");
        when(mockStoryService.updateStory(1, story)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(
                        put("/api/v1/stories/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("utf-8")
                                .content(asJsonString(story)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE/stories/1  SUCCESS")
    void deleteStorySuccess() throws Exception {
        doNothing().when(mockStoryService).deleteStory(1);

        mockMvc.perform(delete("/api/v1/stories/{id}", 1)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE/stories/1  FAILED")
    void deleteStoryFailed() throws Exception {
        doThrow(AccessDeniedException.class).when(mockStoryService).deleteStory(1);

        mockMvc.perform(delete("/api/v1/stories/{id}", 1)).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET/stories/my  Success")
    void getMyStory() throws Exception {
        StoryDto storyDto = new StoryDto(1, "haseb@gmail.com", "Spring", "Spring boot is a magic");
        when(mockStoryService.getMyStory()).thenReturn(Arrays.asList(storyDto));

        mockMvc.perform(get("/api/v1/stories/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("haseb@gmail.com"));
    }

    @Test
    @DisplayName("GET/users/1/stories  Success")
    void getStoryOfUser() throws Exception {
        StoryDto storyDto = new StoryDto(1, "haseb@gmail.com", "Spring", "Spring boot is a magic");
        when(mockStoryService.getStoryByUser(1)).thenReturn(Arrays.asList(storyDto));

        mockMvc.perform(get("/api/v1/users/{id}/stories", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring"));
    }

    @Test
    @DisplayName("GET/stories/page  Success")
    void getStoryPage() throws Exception {
        StoryPageDto pageDto = new StoryPageDto();
        pageDto.setStories(
                Arrays.asList(
                        new StoryDto(1, "haseb@gmail.com", "Spring", "Spring boot is a magic")));
        pageDto.setPageNo(0);
        pageDto.setPageSize(6);
        pageDto.setTotalElements(1);
        pageDto.setTotalPages(1);
        pageDto.setLast(true);
        when(mockStoryService.getStoryPage(0, 6)).thenReturn(pageDto);

        mockMvc.perform(get("/api/v1/stories/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stories[0].title").value("Spring"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    @DisplayName("GET/stories/search  Success")
    void searchStory() throws Exception {
        StoryDto storyDto = new StoryDto(1, "haseb@gmail.com", "Spring", "Spring boot is a magic");
        when(mockStoryService.searchStory("spring")).thenReturn(Arrays.asList(storyDto));

        mockMvc.perform(get("/api/v1/stories/search").param("keyword", "spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
