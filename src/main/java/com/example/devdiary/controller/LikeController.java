package com.example.devdiary.controller;

import com.example.devdiary.dto.LikeDto;
import com.example.devdiary.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${v1API}/stories")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/{id}/likes")
    public ResponseEntity<LikeDto> getLikeOfStory(@PathVariable int id) {

        return ResponseEntity.status(HttpStatus.OK).body(likeService.getLikeOfStory(id));
    }

    // like or unlike a story
    @PostMapping("/{id}/likes")
    public ResponseEntity<LikeDto> toggleLike(@PathVariable int id) {

        return ResponseEntity.status(HttpStatus.OK).body(likeService.toggleLike(id));
    }
}
