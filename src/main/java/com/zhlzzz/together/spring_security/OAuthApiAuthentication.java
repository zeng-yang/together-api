package com.zhlzzz.together.spring_security;

import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class OAuthApiAuthentication implements ApiAuthentication {

    private final OAuth2Authentication oAuth2Authentication;

    @Override
    public Optional<Long> getUserId() {
        if (oAuth2Authentication.isClientOnly()) {
            return Optional.empty();
        }

        if (oAuth2Authentication.getUserAuthentication() == null) {
            return Optional.empty();
        }

        try {
            Long id = Long.parseLong(oAuth2Authentication.getUserAuthentication().getPrincipal().toString());
            return Optional.of(id);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public String getClientKey() {
        return getOAuth2Request().getClientId();
    }

    @Override
    public Long requireUserId() {
        return getUserId().orElseThrow(() -> ApiExceptions.needLogin());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2Authentication.getAuthorities();
    }

    @Override
    public Set<String> getScope() {
        return oAuth2Authentication.getOAuth2Request().getScope();
    }

    public OAuth2Request getOAuth2Request() {
        return oAuth2Authentication.getOAuth2Request();
    }
}
