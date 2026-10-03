package com.example.devdiary.security;

import com.example.devdiary.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class Authenticate {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsInfo userDetailsInfo;

    public String authenticate(Users users){


        final UserDetails userDetails=userDetailsInfo.loadUserByUsername(users.getEmail());
        final String token= jwtUtil.generateToken(userDetails);
        return token;

    }
}
