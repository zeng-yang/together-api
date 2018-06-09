package com.zhlzzz.together.auth.oauth;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends Repository<AccessToken, Long> {

    AccessToken save(AccessToken token);
    void removeAccessTokenByToken(String token);
    void removeAccessTokenByRefreshToken(String refreshToken);
    Optional<AccessToken> getAccessTokenByToken(String token);
    Optional<AccessToken> getAccessTokenByRefreshToken(String refreshToken);
    List<AccessToken> getAccessTokensByClientIdAndUserId(Long clientId, Long userId);
}
