package com.zhlzzz.together.controllers;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ApiAuthentication {
    Optional<Long> getUserId();
    Long requireUserId();
    String getClientKey();
    Collection<? extends GrantedAuthority> getAuthorities();
    Set<String> getScope();
}
