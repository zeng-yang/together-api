package com.zhlzzz.together.spring_security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nullable;

public class UserIdExtractor {
    @Nullable
    public static Long getUserIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long) {
            return (Long)principal;
        } else if (principal instanceof String) {
            try {
                return Long.parseLong((String)principal);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (principal instanceof UserDetailImpl) {
            return ((UserDetailImpl) principal).getUserId();
        } else if (principal instanceof UserDetails) {
            try {
                return Long.parseLong(((UserDetails) principal).getUsername());
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
