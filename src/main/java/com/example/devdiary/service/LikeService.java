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

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final IsValidStory checkAuth;

    public LikeService(
            LikeRepository likeRepository,
            StoryRepository storyRepository,
            UserRepository userRepository,
            IsValidStory checkAuth) {
        this.likeRepository = likeRepository;
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.checkAuth = checkAuth;
    }

    public LikeDto getLikeOfStory(int storyId) {
        findStory(storyId);
        List<String> users =
                likeRepository.findByStory_Id(storyId).stream()
                        .map(like -> like.getUser().getEmail())
                        .toList();
        return new LikeDto(users.size(), users);
    }

    public LikeDto toggleLike(int storyId) {
        Storys story = findStory(storyId);
        String email = checkAuth.getAuthName();
        Optional<Likes> existing = likeRepository.findByStory_IdAndUser_Email(storyId, email);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
        } else {
            Users user =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(
                                    () -> new EntityNotFoundException(Users.class, "email", email));
            likeRepository.save(new Likes(user, story));
        }
        return getLikeOfStory(storyId);
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
