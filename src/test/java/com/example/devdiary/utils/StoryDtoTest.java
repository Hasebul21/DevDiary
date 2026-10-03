package com.example.devdiary.utils;

import com.example.devdiary.dto.StoryDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoryDtoTest {

    @Test
    void check_All_Setter_Getter() {
        StoryDto storyDto = new StoryDto();
        storyDto.setId(1);
        storyDto.setAuthor("haseb@gmail.com");
        storyDto.setTitle("ABCDE");
        storyDto.setDescription("My name is Ragnar Lothbrok");
        Assertions.assertEquals(1, storyDto.getId(), "Id should be one");
        Assertions.assertEquals("ABCDE", storyDto.getTitle(), "Title should be one");
        Assertions.assertEquals("haseb@gmail.com", storyDto.getAuthor(), "Author Should be same");
        Assertions.assertEquals(
                "My name is Ragnar Lothbrok",
                storyDto.getDescription(),
                "Description should be one");
    }

    @Test
    void check_Constructor() {
        StoryDto storyDto =
                new StoryDto(1, "haseb@gmail.com", "ABCDE", "My name is Ragnar Lothbrok");
        Assertions.assertEquals(1, storyDto.getId(), "Id should be one");
        Assertions.assertEquals("ABCDE", storyDto.getTitle(), "Title should be one");
        Assertions.assertEquals("haseb@gmail.com", storyDto.getAuthor(), "Author Should be same");
        Assertions.assertEquals(
                "My name is Ragnar Lothbrok",
                storyDto.getDescription(),
                "Description should be one");
    }
}
