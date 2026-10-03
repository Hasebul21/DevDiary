package com.example.devdiary.controller;

import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.service.StoryService;
import com.example.devdiary.service.TagService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${v1API}/tags")
public class TagController {

    private final TagService tagService;
    private final StoryService storyService;

    public TagController(TagService tagService, StoryService storyService) {
        this.tagService = tagService;
        this.storyService = storyService;
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> getAllTag() {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getAllTag());
    }

    @GetMapping("/{name}/stories")
    public ResponseEntity<List<StoryDto>> getStoryByTag(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(storyService.getStoryByTag(name));
    }
}
