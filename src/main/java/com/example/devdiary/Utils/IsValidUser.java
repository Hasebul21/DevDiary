package com.example.devdiary.Utils;

import com.example.devdiary.entity.Users;
import com.example.devdiary.security.IAuthenticationFacade;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class IsValidUser {

    private final IAuthenticationFacade authenticationFacade;

    public IsValidUser(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public boolean isValid(Users user) {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.isAuthenticated() && authentication.getName().equals(user.getEmail());
    }
}
