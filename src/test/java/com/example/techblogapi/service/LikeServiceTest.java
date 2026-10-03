package com.example.techblogapi.service;

import com.example.techblogapi.Utils.IsValidStory;
import com.example.techblogapi.dto.LikeDto;
import com.example.techblogapi.entity.Likes;
import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.repository.LikeRepository;
import com.example.techblogapi.repository.StoryRepository;
import com.example.techblogapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @MockBean
    private LikeRepository likeRepository;

    @MockBean
    private StoryRepository storyRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private IsValidStory checkAuth;


    @Test
    @DisplayName("Test Get Like Of Story")
    void getLikeOfStory(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(likeRepository.findByStory_Id(1)).thenReturn(Arrays.asList(new Likes(mockUser,mockStory)));
        LikeDto likeDto=likeService.getLikeOfStory(1);
        Assertions.assertEquals(1,likeDto.getCount(),"Expected 1 Like");
        Assertions.assertEquals("haseb@gmail.com",likeDto.getUsers().get(0),"User should be same");
    }

    @Test
    @DisplayName("Test Like A Story")
    void likeStory(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(checkAuth.getAuthName()).thenReturn("haseb@gmail.com");
        when(likeRepository.findByStory_IdAndUser_Email(1,"haseb@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.of(mockUser));
        when(likeRepository.findByStory_Id(1)).thenReturn(Arrays.asList(new Likes(mockUser,mockStory)));
        LikeDto likeDto=likeService.toggleLike(1);
        verify(likeRepository,times(1)).save(any(Likes.class));
        Assertions.assertEquals(1,likeDto.getCount(),"Expected 1 Like");
    }

    @Test
    @DisplayName("Test Unlike A Story")
    void unlikeStory(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        Likes oldLike=new Likes(mockUser,mockStory);
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(checkAuth.getAuthName()).thenReturn("haseb@gmail.com");
        when(likeRepository.findByStory_IdAndUser_Email(1,"haseb@gmail.com")).thenReturn(Optional.of(oldLike));
        when(likeRepository.findByStory_Id(1)).thenReturn(Collections.emptyList());
        LikeDto likeDto=likeService.toggleLike(1);
        verify(likeRepository,times(1)).delete(oldLike);
        verify(likeRepository,never()).save(any(Likes.class));
        Assertions.assertEquals(0,likeDto.getCount(),"Expected 0 Like");
    }
}
