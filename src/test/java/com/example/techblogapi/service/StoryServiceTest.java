package com.example.techblogapi.service;

import com.example.techblogapi.Utils.IsValidStory;
import com.example.techblogapi.dto.StoryDto;
import com.example.techblogapi.dto.StoryPageDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.techblogapi.dto.StoryDtoConverter;
import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.exception.AccessDeniedException;
import com.example.techblogapi.exception.EntityNotFoundException;
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
public class StoryServiceTest {

    @Autowired
    private StoryService storyService;

    @MockBean
    private StoryRepository storyRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private StoryDtoConverter storyDtoConverter;

    @MockBean
    private IsValidStory checkAuth;


    @Test
    @DisplayName("Test to get all Story")
    void getAllStory(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory1=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        Storys mockStory2=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findAll()).thenReturn(Arrays.asList(mockStory1,mockStory2));
        List<StoryDto>allStory=storyService.getAllStory();
        Assertions.assertEquals(2,allStory.size(),"Expected 2 Story");
    }

    @Test
    @DisplayName("Test Find Single Story Success")
    void getSingleStorySuccess(){

        Storys story=new Storys();
        StoryDto mockstoryDto=new StoryDto(1,"haseb@gmail.com","Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(story));
        when(storyDtoConverter.getDetails(story)).thenReturn(mockstoryDto);
        StoryDto expectedStory=storyService.getSingleStory(1);
        Assertions.assertSame(mockstoryDto,expectedStory,"Story should be same");

    }

    @Test
    @DisplayName("Test Find Single Story Failed")
    void getSingleStoryFailed(){

        when(storyRepository.findById(1)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class,()->storyService.getSingleStory(1),"This should be Throw an Exception");

    }

    @Test
    @DisplayName("Test Post Single Story Success")
    void postStorySucess(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        StoryDto mockstoryDto=new StoryDto(1,"haseb@gmail.com","Spring boot","Spring boot is hard.Really!!!!");
        when(checkAuth.getAuthName()).thenReturn("haseb@gmail.com");
        when(userRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.of(mockUser));
        when(storyRepository.save(mockStory)).thenReturn(mockStory);
        when(storyDtoConverter.getDetails(mockStory)).thenReturn(mockstoryDto);
        StoryDto expected=storyService.postStory(mockStory);
        Assertions.assertSame(expected,mockstoryDto,"Story Should be Same");

    }

    @Test
    @DisplayName("Test Post Single Story Failed")
    void postStoryFailed(){

        when(checkAuth.getAuthName()).thenThrow(AccessDeniedException.class);
        Assertions.assertThrows(AccessDeniedException.class,()->storyService.postStory(new Storys()),"This should throw an exception");

    }

    @Test
    @DisplayName("Test Update Single Story Success")
    void updateStorySuccess(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        StoryDto mockstoryDto=new StoryDto(1,"haseb@gmail.com","Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(checkAuth.isValid(Optional.of(mockStory))).thenReturn(true);
        when(storyRepository.save(mockStory)).thenReturn(mockStory);
        when(storyDtoConverter.getDetails(mockStory)).thenReturn(mockstoryDto);
        StoryDto expected=storyService.updateStory(1,mockStory);
        Assertions.assertSame(expected,mockstoryDto,"Story Should be Same");

    }

    @Test
    @DisplayName("Test Update Single Story Failed")
    void updateStoryFailed(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(checkAuth.isValid(Optional.of(mockStory))).thenThrow(AccessDeniedException.class);
        Assertions.assertThrows(AccessDeniedException.class,()->storyService.updateStory(1,mockStory),"This should throw an exception");

    }

    @Test
    @DisplayName("Test Delete Single Story Success")
    void deleteStorySuccess(){

        StoryService mockStoryService=mock(StoryService.class);
        mockStoryService.deleteStory(1);
        verify(mockStoryService,times(1)).deleteStory(1);
    }

    @Test
    @DisplayName("Test Delete Single Story Failed")
    void deleteStoryFailed(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findById(1)).thenReturn(Optional.of(mockStory));
        when(checkAuth.isValid(Optional.of(mockStory))).thenReturn(false);
        Assertions.assertThrows(AccessDeniedException.class,()->storyService.deleteStory(1),"This should throw an exception");
    }

    @Test
    @DisplayName("Test Get My Story")
    void getMyStory(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory1=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        Storys mockStory2=new Storys(2, mockUser,"Java","Java is good");
        when(checkAuth.getAuthName()).thenReturn("haseb@gmail.com");
        when(storyRepository.findByAuthorid_EmailOrderByIdDesc("haseb@gmail.com")).thenReturn(Arrays.asList(mockStory1,mockStory2));
        List<StoryDto>myStory=storyService.getMyStory();
        Assertions.assertEquals(2,myStory.size(),"Expected 2 Story");
    }

    @Test
    @DisplayName("Test Get Story By User Success")
    void getStoryByUserSuccess(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory1=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(storyRepository.findByAuthorid_IdOrderByIdDesc(1)).thenReturn(Arrays.asList(mockStory1));
        List<StoryDto>userStory=storyService.getStoryByUser(1);
        Assertions.assertEquals(1,userStory.size(),"Expected 1 Story");
    }

    @Test
    @DisplayName("Test Get Story By User Failed")
    void getStoryByUserFailed(){

        when(userRepository.findById(5)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,()->storyService.getStoryByUser(5),"This should throw an exception");
    }

    @Test
    @DisplayName("Test Get Story Page")
    void getStoryPage(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Storys mockStory1=new Storys(2, mockUser,"Java","Java is good");
        Storys mockStory2=new Storys(1, mockUser,"Spring boot","Spring boot is hard.Really!!!!");
        when(storyRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(mockStory1,mockStory2), PageRequest.of(0,2),5));
        StoryPageDto page=storyService.getStoryPage(0,2);
        Assertions.assertEquals(2,page.getStories().size(),"Expected 2 Story");
        Assertions.assertEquals(5,page.getTotalElements(),"Total should be 5");
        Assertions.assertEquals(3,page.getTotalPages(),"Total page should be 3");
        Assertions.assertFalse(page.isLast(),"This is not the last page");
    }
}
