package com.example.devdiary.dto;

import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Tags;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoryDtoConverter {

    public StoryDto getDetails(Storys story) {
        StoryDto storyDto = new StoryDto();
        storyDto.setId(story.getId());
        storyDto.setTitle(story.getTitle());
        storyDto.setDescription(story.getDescription());
        storyDto.setAuthor(story.getAuthorid().getEmail());
        storyDto.setCreatedDate(story.getCreatedDate());
        storyDto.setTags(
                story.getTags() == null
                        ? List.of()
                        : story.getTags().stream().map(Tags::getName).toList());
        return storyDto;
    }
}
