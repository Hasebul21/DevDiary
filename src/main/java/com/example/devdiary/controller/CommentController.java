package com.example.devdiary.controller;

import com.example.devdiary.dto.CommentDto;
import com.example.devdiary.entity.Comments;
import com.example.devdiary.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${v1API}")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/stories/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentOfStory(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentOfStory(id));
    }

    @PostMapping("/stories/{id}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable int id, @RequestBody Comments comment) {
        CommentDto newComment = commentService.addComment(id, comment);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
