package com.example.devdiary.Utils;

import com.example.devdiary.entity.Storys;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.security.IAuthenticationFacade;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IsValidStory {

    private final IAuthenticationFacade authenticationFacade;

    public IsValidStory(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public boolean isValid(Optional<Storys> story) {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (!authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getName().equals(story.get().getAuthorid().getEmail());
    }

    public boolean isAdmin() {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    public String getAuthName() {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new AccessDeniedException(authentication.getName() + " is not Authenticated");
        }
        return authentication.getName();
    }
}
