package com.ead.authuser.validations;

import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ead.authuser.configs.security.AuthenticationCurrentUserService;

@Component("userAuthorization")
public class UserAuthorization {

    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserAuthorization(AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    public boolean isCurrentUserOrAdmin(UUID userId) {
        var currentUser = authenticationCurrentUserService.getCurrentUser();

        return currentUser.getUserId().equals(userId) ||
                currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
