package com.example.devdiary.controller;

import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.dto.UserDto;
import com.example.devdiary.entity.Users;
import com.example.devdiary.service.StoryService;
import com.example.devdiary.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${v1API}/users")
public class UserController {

    private final UserService userService;
    private final StoryService storyService;

    public UserController(UserService userService, StoryService storyService) {
        this.userService = userService;
        this.storyService = storyService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleUser(@PathVariable int id) {
        UserDto newUsers = userService.getSingleUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(newUsers);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getMe());
    }

    @GetMapping("/{id}/stories")
    public ResponseEntity<List<StoryDto>> getStoryOfUser(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(storyService.getStoryByUser(id));
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody Users users) {
        UserDto newUsers = userService.updateUser(id, users);
        return new ResponseEntity<>(newUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
