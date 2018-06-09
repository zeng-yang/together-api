package com.zhlzzz.together.auth.oauth;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface TokenService {

    AccessToken addAccessToken(String token, String grantType, Long clientId, @Nullable Long userId, @Nullable LocalDateTime expiration, @Nullable Set<String> scope, @Nullable String refreshToken, @Nullable LocalDateTime refreshTokenExpiration);
    void removeAccessTokenByToken(String token);
    void removeAccessTokenByRefreshToken(String refreshToken);
    Optional<AccessToken> getAccessTokenByToken(String token);
    Optional<AccessToken> getAccessTokenByRefreshToken(String refreshToken);
    Optional<AccessToken> getAccessTokenByClientIdAndUserId(Long clientId, Long userId);

}
