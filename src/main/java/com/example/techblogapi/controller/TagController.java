package com.example.techblogapi.controller;

import com.example.techblogapi.dto.StoryDto;
import com.example.techblogapi.service.StoryService;
import com.example.techblogapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${v1API}/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private StoryService storyService;

    @GetMapping("/")
    public ResponseEntity<List<String>> getAllTag() {

        return ResponseEntity.status(HttpStatus.OK).body(tagService.getAllTag());
    }

    // all stories with this tag
    @GetMapping("/{name}/stories")
    public ResponseEntity<List<StoryDto>> getStoryByTag(@PathVariable String name) {

        return ResponseEntity.status(HttpStatus.OK).body(storyService.getStoryByTag(name));
    }
}
