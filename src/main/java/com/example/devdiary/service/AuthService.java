package com.example.devdiary.service;

import com.example.devdiary.Utils.PasswordValidator;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.DuplicateEmailException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.exception.InvalidPasswordException;
import com.example.devdiary.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(
            PasswordValidator passwordValidator,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Users signUp(Users user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException(email + " already exist");
        }
        if (!passwordValidator.isValid(user.getPassword())) {
            throw new InvalidPasswordException();
        }
        user.setId(0);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Users signIn(Users user) {
        String email = user.getEmail();
        Users existing =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new EntityNotFoundException(Users.class, "Email", email));
        if (!passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
            throw new AccessDeniedException("Invalid email or password");
        }
        return existing;
    }
}
