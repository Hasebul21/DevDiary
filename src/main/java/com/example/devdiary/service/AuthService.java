package com.example.devdiary.service;

import com.example.devdiary.Utils.PasswordValidator;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.DuplicateEmailException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.exception.InvalidPasswordException;
import com.example.devdiary.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    public static final String GUEST_EMAIL = "guest@devdiary.com";
    public static final String GUEST_ROLE = "GUEST";

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
        if (email != null && GUEST_EMAIL.equalsIgnoreCase(email.trim())) {
            throw new DuplicateEmailException(email + " already exist");
        }
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

    public Users signInAsGuest() {
        Users guest = userRepository.findByEmail(GUEST_EMAIL).orElseGet(this::createGuest);
        if (!GUEST_ROLE.equals(guest.getRole())) {
            throw new AccessDeniedException("Guest account unavailable");
        }
        return guest;
    }

    private Users createGuest() {
        Users guest = new Users();
        guest.setEmail(GUEST_EMAIL);
        guest.setName("Guest User");
        guest.setPhone("01800000000");
        guest.setRole(GUEST_ROLE);
        guest.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        try {
            return userRepository.save(guest);
        } catch (DataIntegrityViolationException e) {
            return userRepository.findByEmail(GUEST_EMAIL).orElseThrow(() -> e);
        }
    }
}
