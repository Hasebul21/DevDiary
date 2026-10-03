package com.example.devdiary.controller;

import com.example.devdiary.entity.Users;
import com.example.devdiary.security.Authenticate;
import com.example.devdiary.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "${v1API}")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private Authenticate authenticate;

    @PostMapping(path="/signup")
    public ResponseEntity<?> signUp(@RequestBody Users users)  {
        Users newUser =authService.signUp(users);
        String token=authenticate.authenticate(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping(path="/signin")
    public ResponseEntity<?> signIn(@RequestBody Users users) {
        Users newUser = authService.signIn(users);
        String token=authenticate.authenticate(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}