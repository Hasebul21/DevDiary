package com.example.devdiary.utils;

import com.example.devdiary.dto.UserDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDtoTest {

    @Test
    void check_all_setter_getter() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("Hasebul");
        userDto.setEmail("haseb@gmail.com");
        userDto.setPhone("01789533586");

        Assertions.assertEquals(1, userDto.getId(), "Id should be one");
        Assertions.assertEquals("Hasebul", userDto.getName(), "Name should be one");
        Assertions.assertEquals("haseb@gmail.com", userDto.getEmail(), "Author Should be same");
        Assertions.assertEquals("01789533586", userDto.getPhone(), "Phone should be same");
    }

    @Test
    void check_Constructor() {
        UserDto userDto = new UserDto(1, "haseb@gmail.com", "Hasebul", "01789533586");
        Assertions.assertEquals(1, userDto.getId(), "Id should be one");
        Assertions.assertEquals("Hasebul", userDto.getName(), "Name should be one");
        Assertions.assertEquals("haseb@gmail.com", userDto.getEmail(), "Author Should be same");
        Assertions.assertEquals("01789533586", userDto.getPhone(), "Phone should be same");
    }
}
