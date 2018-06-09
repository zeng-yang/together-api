package com.zhlzzz.together.spring_security;

import com.zhlzzz.together.auth.password.UserPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TokenEnhancerImpl implements TokenEnhancer {

    private final UserPasswordService userPasswordService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken && !authentication.isClientOnly()) {
            try {
                Long userId = UserIdExtractor.getUserIdFromAuthentication(authentication.getUserAuthentication());
                if (userId == null) {
                    return accessToken;
                }

                DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken)accessToken;
                Map<String, Object> infos = new LinkedHashMap<>();
                infos.put("user_id", userId);


                defaultAccessToken.setAdditionalInformation(infos);
            } catch (NumberFormatException e) {
                return accessToken;
            }
        }
        return accessToken;
    }
}
