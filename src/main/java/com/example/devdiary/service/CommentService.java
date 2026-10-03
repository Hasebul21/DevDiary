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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IsValidStory checkAuth;

    @Autowired
    private CommentDtoConverter commentDtoConverter;


    public List<CommentDto> getCommentOfStory(int storyId) {

        Optional<Storys> story=storyRepository.findById(storyId);
        if(story.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(storyId));

        List<Comments> allComment=commentRepository.findByStory_IdOrderByIdAsc(storyId);
        List<CommentDto> result=new ArrayList<>();
        for(Comments comment : allComment){
            result.add(commentDtoConverter.getDetails(comment));
        }
        return result;
    }

    public CommentDto addComment(int storyId, Comments comment) {

        Optional<Storys> story=storyRepository.findById(storyId);
        if(story.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(storyId));

        String userEmail=checkAuth.getAuthName();
        Optional<Users> currentUser=userRepository.findByEmail(userEmail);
        if(currentUser.isEmpty()) throw new EntityNotFoundException(Users.class,"email",userEmail);

        // make a new comment so that client can not set id or user
        Comments newComment=new Comments();
        newComment.setText(comment.getText());
        newComment.setUser(currentUser.get());
        newComment.setStory(story.get());
        commentRepository.save(newComment);
        return commentDtoConverter.getDetails(newComment);
    }

    public void deleteComment(int commentId) {

        Optional<Comments> comment=commentRepository.findById(commentId);
        if(comment.isEmpty()) throw new EntityNotFoundException(Comments.class,"id",String.valueOf(commentId));

        // only the person who wrote the comment or admin can delete it
        String userEmail=checkAuth.getAuthName();
        if(!comment.get().getUser().getEmail().equals(userEmail) && !checkAuth.isAdmin()) throw new AccessDeniedException("Unauthorized user");

        commentRepository.deleteById(commentId);
    }
}
