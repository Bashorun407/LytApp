package com.bash.LytApp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {
    public UserPrincipal getPrincipal() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new IllegalStateException("No authenticated user found");
        }
        return (UserPrincipal) auth.getPrincipal();
    }

    public Long getUserId() {
        return getPrincipal().getId();
    }

    public String getEmail() {
        return getPrincipal().getUsername();
    }
}
