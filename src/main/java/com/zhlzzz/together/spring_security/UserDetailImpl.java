package com.zhlzzz.together.spring_security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailImpl implements UserDetails {

    private static long serialVersionUID = 4447448038122101931L;

    private Collection<? extends GrantedAuthority> authorities;

    private Long userId;
    private String password;

    UserDetailImpl(Long userId, String password) {
        this.userId = userId;
        this.password = password;
        this.authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
