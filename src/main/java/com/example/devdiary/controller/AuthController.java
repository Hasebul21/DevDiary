package com.example.devdiary.controller;

import com.example.devdiary.entity.Users;
import com.example.devdiary.security.Authenticate;
import com.example.devdiary.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${v1API}")
public class AuthController {

    private final AuthService authService;
    private final Authenticate authenticate;

    public AuthController(AuthService authService, Authenticate authenticate) {
        this.authService = authService;
        this.authenticate = authenticate;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUp(@RequestBody Users users) {
        Users newUser = authService.signUp(users);
        String token = authenticate.authenticate(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<?> signIn(@RequestBody Users users) {
        Users newUser = authService.signIn(users);
        String token = authenticate.authenticate(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping(path = "/signin/guest")
    public ResponseEntity<?> signInAsGuest() {
        Users guest = authService.signInAsGuest();
        String token = authenticate.authenticate(guest);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
