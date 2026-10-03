package com.example.devdiary.service;

import com.example.devdiary.Utils.IsValidStory;
import com.example.devdiary.dto.CommentDto;
import com.example.devdiary.dto.CommentDtoConverter;
import com.example.devdiary.entity.Comments;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.repository.CommentRepository;
import com.example.devdiary.repository.StoryRepository;
import com.example.devdiary.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final IsValidStory checkAuth;
    private final CommentDtoConverter commentDtoConverter;

    public CommentService(
            CommentRepository commentRepository,
            StoryRepository storyRepository,
            UserRepository userRepository,
            IsValidStory checkAuth,
            CommentDtoConverter commentDtoConverter) {
        this.commentRepository = commentRepository;
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.checkAuth = checkAuth;
        this.commentDtoConverter = commentDtoConverter;
    }

    public List<CommentDto> getCommentOfStory(int storyId) {
        findStory(storyId);
        return commentRepository.findByStory_IdOrderByIdAsc(storyId).stream()
                .map(commentDtoConverter::getDetails)
                .toList();
    }

    public CommentDto addComment(int storyId, Comments request) {
        Storys story = findStory(storyId);
        String email = checkAuth.getAuthName();
        Users author =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new EntityNotFoundException(Users.class, "email", email));

        Comments comment = new Comments();
        comment.setText(request.getText());
        comment.setUser(author);
        comment.setStory(story);
        commentRepository.save(comment);
        return commentDtoConverter.getDetails(comment);
    }

    public void deleteComment(int commentId) {
        Comments comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                Comments.class, "id", String.valueOf(commentId)));
        boolean isAuthor = comment.getUser().getEmail().equals(checkAuth.getAuthName());
        if (!isAuthor && !checkAuth.isAdmin()) {
            throw new AccessDeniedException("Unauthorized user");
        }
        commentRepository.deleteById(commentId);
    }

    private Storys findStory(int storyId) {
        return storyRepository
                .findById(storyId)
                .orElseThrow(
                        () ->
                                new EntityNotFoundException(
                                        Storys.class, "id", String.valueOf(storyId)));
    }
}
