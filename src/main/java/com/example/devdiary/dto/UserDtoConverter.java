package com.example.devdiary.dto;

import com.example.devdiary.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto getDetails(Users user){

        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole()==null ? "USER" : user.getRole());
        return userDto;
    }
}
