package com.example.devdiary.security;

import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.repository.UserRepository;
import com.example.devdiary.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsInfo implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Users> newUser=userRepository.findByEmail(email);
        if(newUser.isEmpty()) throw new EntityNotFoundException(Users.class,"email",email);
        Users realUsers =newUser.get();
        // old users do not have role, so they are normal user
        String role=realUsers.getRole()==null ? "USER" : realUsers.getRole();
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        return new User(realUsers.getEmail(), realUsers.getPassword(),authorities);

    }
}
