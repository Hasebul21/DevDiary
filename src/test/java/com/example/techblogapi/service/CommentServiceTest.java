package com.example.techblogapi.service;

import com.example.techblogapi.Utils.IsValidStory;
import com.example.techblogapi.dto.CommentDto;
import com.example.techblogapi.entity.Comments;
import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.exception.AccessDeniedException;
import com.example.techblogapi.exception.EntityNotFoundException;
import com.example.techblogapi.repository.CommentRepository;
import com.example.techblogapi.repository.StoryRepository;
import com.example.techblogapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private StoryRepository storyRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private IsValidStory checkAuth;


    @Test
    @DisplayName("Test Get Comment Of Story")
    void getCommentOfStory(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        Comments comment1=new Comments(1,"Nice post",mockUser,mockStory);
        Comments comment2=new Comments(2,"Thanks",mockUser,mockStory);
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(commentRepository.findByStory_IdOrderByIdAsc(1)).thenReturn(Arrays.asList(comment1,comment2));
        List<CommentDto> comments=commentService.getCommentOfStory(1);
        Assertions.assertEquals(2,comments.size(),"Expected 2 Comment");
        Assertions.assertEquals("haseb@gmail.com",comments.get(0).getAuthor(),"Author should be same");
    }

    @Test
    @DisplayName("Test Get Comment Of Story Not Found")
    void getCommentOfStoryFailed(){

        when(storyRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,()->commentService.getCommentOfStory(1),"This should throw an exception");
    }

    @Test
    @DisplayName("Test Add Comment Success")
    void addCommentSuccess(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(checkAuth.getAuthName()).thenReturn("haseb@gmail.com");
        when(userRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.of(mockUser));
        // client try to set id, it should be ignored
        Comments comment=new Comments(99,"Nice post",null,null);
        CommentDto saved=commentService.addComment(1,comment);
        Assertions.assertEquals("Nice post",saved.getText(),"Text should be same");
        Assertions.assertEquals(0,saved.getId(),"Client id should be ignored");
        Assertions.assertEquals(1,saved.getStoryId(),"Story id should be 1");
        verify(commentRepository,times(1)).save(any(Comments.class));
    }

    @Test
    @DisplayName("Test Delete Comment By Other User")
    void deleteCommentFailed(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        Comments comment=new Comments(1,"Nice post",mockUser,mockStory);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(checkAuth.getAuthName()).thenReturn("other@gmail.com");
        Assertions.assertThrows(AccessDeniedException.class,()->commentService.deleteComment(1),"This should throw an exception");
        verify(commentRepository,never()).deleteById(1);
    }

    @Test
    @DisplayName("Test Delete Comment Success")
    void deleteCommentSuccess(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        Comments comment=new Comments(1,"Nice post",mockUser,mockStory);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(checkAuth.getAuthName()).thenReturn("haseb@gmail.com");
        commentService.deleteComment(1);
        verify(commentRepository,times(1)).deleteById(1);
    }
}
