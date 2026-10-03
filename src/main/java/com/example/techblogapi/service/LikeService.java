package com.example.techblogapi.service;

import com.example.techblogapi.Utils.IsValidStory;
import com.example.techblogapi.dto.LikeDto;
import com.example.techblogapi.entity.Likes;
import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.exception.EntityNotFoundException;
import com.example.techblogapi.repository.LikeRepository;
import com.example.techblogapi.repository.StoryRepository;
import com.example.techblogapi.repository.UserRepository;
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
