package com.zhlzzz.together.auth.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @NonNull
    private final AccessTokenRepository accessTokenRepository;
    private final TransactionTemplate tt;
    @PersistenceContext
    private EntityManager em;

    @Override
    public AccessToken addAccessToken(@NonNull String token, @NonNull String grantType, Long clientId, Long userId, LocalDateTime expiration, Set<String> scope, String refreshToken, LocalDateTime refreshTokenExpiration) {
        LocalDateTime now = LocalDateTime.now();

        ScopeStringConverter scopeConverter = new ScopeStringConverter();

        tt.execute((s)->{
            em.createNamedQuery(AccessToken.ADD_QUERY_NAME)
                    .setParameter("token", token)
                    .setParameter("grantType", grantType)
                    .setParameter("clientId", clientId)
                    .setParameter("userId", userId)
                    .setParameter("createTime", now)
                    .setParameter("expiration", expiration)
                    .setParameter("scope", scopeConverter.convertToDatabaseColumn(scope))
                    .setParameter("refreshToken", refreshToken)
                    .setParameter("refreshTokenCreateTime", now)
                    .setParameter("refreshTokenExpiration", refreshTokenExpiration)
                    .executeUpdate();
            return null;
        });

        return getAccessTokenByClientIdAndUserId(userId, clientId).orElse(null);
    }

    @Override
    public void removeAccessTokenByToken(String token) {
        accessTokenRepository.removeAccessTokenByToken(token);
    }

    @Override
    public void removeAccessTokenByRefreshToken(String refreshToken) {
        accessTokenRepository.removeAccessTokenByRefreshToken(refreshToken);
    }

    @Override
    public Optional<AccessToken> getAccessTokenByToken(String token) {
        return accessTokenRepository.getAccessTokenByToken(token);
    }

    @Override
    public Optional<AccessToken> getAccessTokenByRefreshToken(String refreshToken) {
        return accessTokenRepository.getAccessTokenByRefreshToken(refreshToken);
    }

    @Override
    public Optional<AccessToken> getAccessTokenByClientIdAndUserId(Long clientId, Long userId) {
        List<AccessToken> accessTokens = em.createQuery("SELECT a FROM AccessToken a WHERE a.userId = :userId AND a.clientId = :clientId ORDER BY a.id DESC", AccessToken.class)
                .setParameter("clientId", clientId)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList();

        if (accessTokens.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(accessTokens.get(0));
        }
    }

}
