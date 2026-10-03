package com.example.devdiary.controller;

import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.security.JwtFilter;
import com.example.devdiary.service.StoryService;
import com.example.devdiary.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {

    @MockBean
    private TagService mockTagService;

    @MockBean
    private StoryService mockStoryService;

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
    @DisplayName("GET/tags  Success")
    void getAllTag() throws Exception{

        when(mockTagService.getAllTag()).thenReturn(Arrays.asList("java","spring"));

        mockMvc.perform(get("/api/v1/tags/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("java"))
                .andExpect(jsonPath("$[1]").value("spring"));
    }

    @Test
    @DisplayName("GET/tags/java/stories  Success")
    void getStoryByTag() throws Exception{

        StoryDto storyDto=new StoryDto(1,"haseb@gmail.com","Spring","Spring boot is a magic");
        storyDto.setTags(Arrays.asList("java"));
        when(mockStoryService.getStoryByTag("java")).thenReturn(Arrays.asList(storyDto));

        mockMvc.perform(get("/api/v1/tags/{name}/stories","java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring"))
                .andExpect(jsonPath("$[0].tags[0]").value("java"));
    }
}
