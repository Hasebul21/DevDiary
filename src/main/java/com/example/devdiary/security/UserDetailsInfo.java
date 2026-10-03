package com.example.devdiary.security;

import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsInfo implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsInfo(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new EntityNotFoundException(Users.class, "email", email));
        String role = user.getRole() == null ? "USER" : user.getRole();
        return new User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }
}
