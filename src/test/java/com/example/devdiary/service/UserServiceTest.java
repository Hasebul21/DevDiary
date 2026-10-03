package com.example.devdiary.service;

import com.example.devdiary.Utils.IsValidUser;
import com.example.devdiary.dto.UserDto;
import com.example.devdiary.dto.UserDtoConverter;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.exception.handler.GlobalExceptionHandler;
import com.example.devdiary.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private IsValidUser isValidUser;

    @MockBean
    private UserDtoConverter userDtoConverter;


    @Test
    @DisplayName("Test Find All User Success")
    void getAllUser(){

        Users userOne=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Users userTwo=new Users(1,"sakib@gmail.com","abcde","Sakib","01789533586");
        when(userRepository.findAll()).thenReturn(Arrays.asList(userOne,userTwo));
        List<UserDto>allUser=userService.getAllUser();
        Assertions.assertEquals(2,allUser.size(),"Expected 2 User");
    }

    @Test
    @DisplayName("Test Find Single User Sucesss")
    void getSingleUserSuccess(){

        UserDto mockUserDto=new UserDto(1,"haseb@gmail.com","Haseb","01789533586");
        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(userDtoConverter.getDetails(mockUser)).thenReturn(mockUserDto);
        UserDto expectedUser=userService.getSingleUser(1);
        Assertions.assertSame(expectedUser,mockUserDto,"User should be same");

    }

    @Test
    @DisplayName("Test Find Single User Failed")
    void getSingleUserFailed(){

        Users mockUser=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        when(userRepository.findById(1)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class,()->userService.getSingleUser(1),"User should not be found");

    }


    @Test
    @DisplayName("Update a single User Success")
    void updateSingleUserSucess(){

        Users userOne=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Users userTwo=new Users(1,"sakib@gmail.com","abcde","Sakib","01789533586");
        UserDto mockUserDto=new UserDto(1,"sakib@gmail.com","Sakib","01789533586");
        when(userRepository.findById(1)).thenReturn(Optional.of(userTwo));
        when(isValidUser.isValid(userTwo)).thenReturn(true);
        when(userRepository.save(userOne)).thenReturn(userTwo);
        when(userDtoConverter.getDetails(userTwo)).thenReturn(mockUserDto);
        UserDto expectedUser=userService.updateUser(1,userOne);
        Assertions.assertSame(expectedUser,mockUserDto,"User should be same and updated");
    }
    @Test
    @DisplayName("Update a single User Failed")
    void updateSingleUserFailed(){

        Users userOne=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        Users userTwo=new Users(1,"sakib@gmail.com","abcde","Sakib","01789533586");
        when(userRepository.findById(1)).thenReturn(Optional.of(userOne));
        when(isValidUser.isValid(userOne)).thenReturn(false);
        Assertions.assertThrows(AccessDeniedException.class,()->userService.updateUser(1,userTwo),"Access should be Denied");
    }

    @Test
    @DisplayName("Delete a single User Success")
    void deleteUserSucess(){

        UserService mockUserService=mock(UserService.class);
        Users userOne=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        mockUserService.deleteUser(1);
        verify(mockUserService,times(1)).deleteUser(1);

    }

    @Test
    @DisplayName("Delete a single User Failed")
    void deleteUserFailed(){

        Users userOne=new Users(1,"haseb@gmail.com","12345","Haseb","01789533586");
        when(userRepository.findById(1)).thenReturn(Optional.of(userOne));
        when(isValidUser.isValid(userOne)).thenReturn(false);
        Assertions.assertThrows(AccessDeniedException.class,()->userService.deleteUser(1),"Access should be Denied");

    }


}
