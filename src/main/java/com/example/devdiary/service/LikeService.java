package com.example.devdiary.service;

import com.example.devdiary.Utils.IsValidStory;
import com.example.devdiary.dto.LikeDto;
import com.example.devdiary.entity.Likes;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.repository.LikeRepository;
import com.example.devdiary.repository.StoryRepository;
import com.example.devdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IsValidStory checkAuth;


    public LikeDto getLikeOfStory(int storyId) {

        Optional<Storys> story=storyRepository.findById(storyId);
        if(story.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(storyId));

        List<Likes> allLike=likeRepository.findByStory_Id(storyId);
        List<String> users=new ArrayList<>();
        for(Likes like : allLike){
            users.add(like.getUser().getEmail());
        }
        return new LikeDto(users.size(),users);
    }

    // if user already liked the story then remove the like, otherwise add a like
    public LikeDto toggleLike(int storyId) {

        Optional<Storys> story=storyRepository.findById(storyId);
        if(story.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(storyId));

        String userEmail=checkAuth.getAuthName();
        Optional<Likes> oldLike=likeRepository.findByStory_IdAndUser_Email(storyId,userEmail);
        if(oldLike.isPresent()){
            likeRepository.delete(oldLike.get());
        }
        else{
            Optional<Users> currentUser=userRepository.findByEmail(userEmail);
            if(currentUser.isEmpty()) throw new EntityNotFoundException(Users.class,"email",userEmail);
            likeRepository.save(new Likes(currentUser.get(),story.get()));
        }
        return getLikeOfStory(storyId);
    }
}
