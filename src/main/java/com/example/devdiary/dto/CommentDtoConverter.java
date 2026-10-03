package com.example.devdiary.dto;

import com.example.devdiary.entity.Comments;

import org.springframework.stereotype.Component;

@Component
public class CommentDtoConverter {

    public CommentDto getDetails(Comments comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getUser().getEmail());
        commentDto.setStoryId(comment.getStory().getId());
        commentDto.setCreatedDate(comment.getCreatedDate());
        return commentDto;
    }
}
