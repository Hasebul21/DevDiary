package com.example.devdiary.service;


import com.example.devdiary.Utils.IsValidStory;
import com.example.devdiary.Utils.IsValidUser;
import com.example.devdiary.Utils.PasswordValidator;
import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.dto.UserDto;
import com.example.devdiary.dto.UserDtoConverter;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.exception.InvalidPasswordException;
import com.example.devdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IsValidUser checkAuth;

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Autowired
    private IsValidStory authInfo;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // details of the logged in user
    public UserDto getMe() {

        String email=authInfo.getAuthName();
        Optional<Users> user=userRepository.findByEmail(email);
        if(user.isEmpty()) throw new EntityNotFoundException(Users.class,"email",email);
        return userDtoConverter.getDetails(user.get());
    }

    public List<UserDto> getAllUser() {

        List<Users>allStudent=userRepository.findAll();
        return allStudent.stream().map(x->userDtoConverter.getDetails(x)).toList();
    }

    public UserDto getSingleUser(int id) {

        Optional<Users> checkUser=userRepository.findById(id);
        if(checkUser.isPresent()) {
            UserDto userDto=userDtoConverter.getDetails(checkUser.get());
            return userDto;
        }
        throw new EntityNotFoundException(Users.class,"id",String.valueOf(id));

    }

    public UserDto updateUser(int id, Users users) {

        Optional<Users> newUser=userRepository.findById(id);
        if(newUser.isEmpty())  throw new EntityNotFoundException(Users.class,"id",String.valueOf(id));

        if(checkAuth.isValid(newUser.get())) {

            // email is used inside the login token, so it can not be changed here
            // password is changed only when user sends a new one
            if(users.getPassword()!=null && !users.getPassword().isEmpty()){
                if(!passwordValidator.isValid(users.getPassword())) throw new InvalidPasswordException();
                newUser.get().setPassword(passwordEncoder.encode(users.getPassword()));
            }
            newUser.get().setName(users.getName());
            newUser.get().setPhone(users.getPhone());
            userRepository.save(newUser.get());
            UserDto userDto=userDtoConverter.getDetails(newUser.get());
            return userDto;
        }
        throw new AccessDeniedException("Unauthorized user");

    }

    public void deleteUser(int id) {

        Optional<Users> newUser=userRepository.findById(id);
        if(newUser.isEmpty())  throw new EntityNotFoundException(Users.class,"id",String.valueOf(id));
        if(checkAuth.isValid(newUser.get())) {
            userRepository.deleteById(id);
            return;
        }
        throw new AccessDeniedException("Unauthorized user");
    }

}