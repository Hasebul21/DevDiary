package com.example.techblogapi.service;

import com.example.techblogapi.Utils.PasswordValidator;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.exception.AccessDeniedException;
import com.example.techblogapi.exception.DuplicateEmailException;
import com.example.techblogapi.exception.EntityNotFoundException;
import com.example.techblogapi.exception.InvalidPasswordException;
import com.example.techblogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    public Users signUp(Users user)  {

        String email=user.getEmail();
        Optional<Users>existUser=userRepository.findByEmail(email);
        if(existUser.isPresent()) throw new DuplicateEmailException(email+" already exist");

        if(passwordValidator.isValid(user.getPassword())) {

            String pass=passwordEncoder.encode(user.getPassword());
            user.setPassword(pass);
            // nobody can become admin from signup
            user.setRole("USER");
            return userRepository.save(user);

        }
        throw new InvalidPasswordException();

    }

    public Users signIn(Users user)   {

        String userEmail= user.getEmail();
        String userPassword= user.getPassword();
        Optional<Users> newUser=userRepository.findByEmail(userEmail);
        if(newUser.isEmpty()) throw new EntityNotFoundException(Users.class,"Email",userEmail);
        String hashPass=newUser.get().getPassword();
        if(passwordEncoder.matches(userPassword, hashPass)==false) throw new AccessDeniedException("Invalid email or password");
        return newUser.get();
    }
}