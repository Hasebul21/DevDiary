package com.example.devdiary.service;

import com.example.devdiary.Utils.IsValidStory;
import com.example.devdiary.Utils.IsValidUser;
import com.example.devdiary.Utils.PasswordValidator;
import com.example.devdiary.dto.UserDto;
import com.example.devdiary.dto.UserDtoConverter;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.exception.InvalidPasswordException;
import com.example.devdiary.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final IsValidUser checkAuth;
    private final UserDtoConverter userDtoConverter;
    private final IsValidStory authInfo;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            IsValidUser checkAuth,
            UserDtoConverter userDtoConverter,
            IsValidStory authInfo,
            PasswordValidator passwordValidator,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.checkAuth = checkAuth;
        this.userDtoConverter = userDtoConverter;
        this.authInfo = authInfo;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto getMe() {
        String email = authInfo.getAuthName();
        return userRepository
                .findByEmail(email)
                .map(userDtoConverter::getDetails)
                .orElseThrow(() -> new EntityNotFoundException(Users.class, "email", email));
    }

    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream().map(userDtoConverter::getDetails).toList();
    }

    public UserDto getSingleUser(int id) {
        return userDtoConverter.getDetails(findUser(id));
    }

    public UserDto updateUser(int id, Users request) {
        Users user = findEditableUser(id);
        String password = request.getPassword();
        if (password != null && !password.isEmpty()) {
            if (!passwordValidator.isValid(password)) {
                throw new InvalidPasswordException();
            }
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        userRepository.save(user);
        return userDtoConverter.getDetails(user);
    }

    public void deleteUser(int id) {
        findEditableUser(id);
        userRepository.deleteById(id);
    }

    private Users findUser(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Users.class, "id", String.valueOf(id)));
    }

    private Users findEditableUser(int id) {
        Users user = findUser(id);
        if (!checkAuth.isValid(user)) {
            throw new AccessDeniedException("Unauthorized user");
        }
        if (AuthService.GUEST_ROLE.equals(user.getRole())) {
            throw new AccessDeniedException("Guest account can not be changed");
        }
        return user;
    }
}
