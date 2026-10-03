package com.example.techblogapi.dto;

import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Tags;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StoryDtoConverter {

    public StoryDto getDetails(Storys story){

        StoryDto storyDto=new StoryDto();
        storyDto.setId(story.getId());
        storyDto.setTitle(story.getTitle());
        storyDto.setDescription(story.getDescription());
        storyDto.setAuthor(story.getAuthorid().getEmail());
        storyDto.setCreatedDate(story.getCreatedDate());

        List<String> tagNames=new ArrayList<>();
        if(story.getTags()!=null){
            for(Tags tag : story.getTags()){
                tagNames.add(tag.getName());
            }
        }
        storyDto.setTags(tagNames);
        return storyDto;
    }
}
