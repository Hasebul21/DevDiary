package com.example.devdiary.security;

import com.example.devdiary.entity.Users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Authenticate {

    private final JwtUtil jwtUtil;
    private final UserDetailsInfo userDetailsInfo;

    public Authenticate(JwtUtil jwtUtil, UserDetailsInfo userDetailsInfo) {
        this.jwtUtil = jwtUtil;
        this.userDetailsInfo = userDetailsInfo;
    }

    public String authenticate(Users user) {
        UserDetails userDetails = userDetailsInfo.loadUserByUsername(user.getEmail());
        return jwtUtil.generateToken(userDetails);
    }
}
