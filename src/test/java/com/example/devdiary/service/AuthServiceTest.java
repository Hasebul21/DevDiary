package com.example.devdiary.service;

import static org.mockito.Mockito.when;

import com.example.devdiary.Utils.PasswordValidator;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.DuplicateEmailException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.exception.InvalidPasswordException;
import com.example.devdiary.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class AuthServiceTest {

    @Autowired private AuthService authService;
    @MockBean private PasswordValidator mockpasswordValidator;
    @MockBean private PasswordEncoder mockpasswordEncoder;
    @MockBean private UserRepository mockUserRepository;

    @Test
    @DisplayName("Test Signup for a single user Success")
    void SignUpSuccess() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.empty());
        when(mockpasswordValidator.isValid("12345")).thenReturn(true);
        when(mockpasswordEncoder.encode("12345")).thenReturn("12345");
        when(mockUserRepository.save(userOne)).thenReturn(userOne);
        Users exceptedUser = authService.signUp(userOne);
        Assertions.assertSame(exceptedUser, userOne, "Two User should be same");
    }

    @Test
    @DisplayName("Test Signup for a single user Failed")
    void SignUpFailed() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.empty());
        when(mockpasswordValidator.isValid("12345")).thenReturn(false);
        Assertions.assertThrows(
                InvalidPasswordException.class,
                () -> authService.signUp(userOne),
                "This should throw exception");
    }

    @Test
    @DisplayName("Test Signup for a single user Failed")
    void SignUp_Failed() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.of(userOne));
        Assertions.assertThrows(
                DuplicateEmailException.class,
                () -> authService.signUp(userOne),
                "This should throw exception");
    }

    @Test
    @DisplayName("Test SignIn for a single user Success")
    void SignInSuccess() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.of(userOne));
        when(mockpasswordEncoder.matches("12345", "12345")).thenReturn(true);
        Users exceptedUser = authService.signIn(userOne);
        Assertions.assertSame(exceptedUser, userOne, "Two User should be same");
    }

    @Test
    @DisplayName("Test SignIn for a single user Failed")
    void SignInFailed() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.of(userOne));
        when(mockpasswordEncoder.matches("12345", "12345")).thenReturn(false);
        Assertions.assertThrows(
                AccessDeniedException.class,
                () -> authService.signIn(userOne),
                "This should throw exception");
    }

    @Test
    @DisplayName("Test SignIn for a single user Failed")
    void SignIn_Failed() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> authService.signIn(userOne),
                "This should throw exception");
    }

    @Test
    @DisplayName("Sign Up Can Not Make Admin")
    void SignUpCanNotMakeAdmin() {
        Users userOne = new Users(1, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        userOne.setRole("ADMIN");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.empty());
        when(mockpasswordValidator.isValid("12345")).thenReturn(true);
        when(mockpasswordEncoder.encode("12345")).thenReturn("12345");
        when(mockUserRepository.save(userOne)).thenReturn(userOne);
        Users saved = authService.signUp(userOne);
        Assertions.assertEquals("USER", saved.getRole(), "Role should be USER");
    }

    @Test
    @DisplayName("Sign Up Ignores Id From Client")
    void SignUpIgnoresClientId() {
        Users userOne = new Users(2, "haseb@gmail.com", "12345", "Haseb", "01789533586");
        when(mockUserRepository.findByEmail("haseb@gmail.com")).thenReturn(Optional.empty());
        when(mockpasswordValidator.isValid("12345")).thenReturn(true);
        when(mockpasswordEncoder.encode("12345")).thenReturn("12345");
        when(mockUserRepository.save(userOne)).thenReturn(userOne);
        authService.signUp(userOne);
        Assertions.assertEquals(0, userOne.getId(), "Client id should be ignored");
    }
}
