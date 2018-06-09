package com.zhlzzz.together.spring_security;

import com.zhlzzz.together.auth.oauth.AccessToken;
import com.zhlzzz.together.auth.oauth.Client;
import com.zhlzzz.together.auth.oauth.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class TokenStoreImpl implements TokenStore {

    @NonNull
    private final TokenService tokenService;

    private static class DefaultExpiringOAuth2RefreshToken extends DefaultOAuth2AccessToken implements ExpiringOAuth2RefreshToken {

        private Date expiration;

        DefaultExpiringOAuth2RefreshToken(String value, Date expiration) {
            super(value);
            this.expiration = expiration;
        }

        @Override
        public Date getExpiration() {
            return expiration;
        }
    }

    private OAuth2Request createOAuth2Request(AccessToken accessToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, accessToken.getGrantType());
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        Set<String> scope = accessToken.getScope();
        Map<String, Serializable> extensionProperties = new HashMap<>();
        return new OAuth2Request(parameters, Client.DEFAULT_CLIENT_KEY, authorities, true, scope, null, Client.DEFAULT_CLIENT_REDIRECT_URI, null, extensionProperties);
    }

    private Authentication createUserAuthentication(Long userId) {
        return new UserIdAuthentication(userId);
    }

    private Date localDateTimeToDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private OAuth2Authentication createOAuth2Authentication(AccessToken accessToken) {
        Authentication userAuthentication = accessToken.getUserId() == null ? null : createUserAuthentication(accessToken.getUserId());

        return new OAuth2Authentication(createOAuth2Request(accessToken), userAuthentication);
    }

    private OAuth2AccessToken createOAuth2AccessToken(AccessToken accessToken) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken.getToken());
        if (accessToken.getRefreshToken() != null) {
            token.setRefreshToken(createOAuth2RefreshToken(accessToken));
        }
        if (accessToken.getExpiration() != null) {
            long expirationTime = accessToken.getExpiration().toInstant(ZoneOffset.UTC).getEpochSecond();
            token.setExpiration(new Date(expirationTime * 1000));
        }

        if (accessToken.getScope() != null) {
            token.setScope(accessToken.getScope());
        }

        Map<String, Object> infos = new LinkedHashMap<>();
        infos.put("user_id", accessToken.getUserId());
        token.setAdditionalInformation(infos);

        return token;
    }

    private OAuth2RefreshToken createOAuth2RefreshToken(AccessToken accessToken) {
        if (accessToken.getRefreshToken() != null) {
            return new DefaultExpiringOAuth2RefreshToken(
                    accessToken.getRefreshToken(),
                    localDateTimeToDate(accessToken.getRefreshTokenExpiration()));
        } else {
            return null;
        }
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        AccessToken accessToken = tokenService.getAccessTokenByToken(token).orElse(null);
        if (accessToken == null) {
            return null;
        }
        return createOAuth2Authentication(accessToken);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        LocalDateTime refreshTokenExpiration = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
            if (token.getRefreshToken() instanceof ExpiringOAuth2RefreshToken) {
                refreshTokenExpiration = dateToLocalDateTime(((ExpiringOAuth2RefreshToken) token.getRefreshToken()).getExpiration());
            }
        }
        String grantType = authentication.getOAuth2Request().getGrantType();
        Long userId = authentication.isClientOnly() ? null : UserIdExtractor.getUserIdFromAuthentication(authentication);
        LocalDateTime expiration = dateToLocalDateTime(token.getExpiration());
        tokenService.addAccessToken(token.getValue(), grantType, Client.DEFAULT_CLIENT_ID, userId, expiration, token.getScope(), refreshToken, refreshTokenExpiration);

    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        AccessToken accessToken = tokenService.getAccessTokenByToken(tokenValue).orElse(null);
        if (accessToken == null) {
            return null;
        }
        return createOAuth2AccessToken(accessToken);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        tokenService.removeAccessTokenByToken(token.getValue());
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        // do nothing.
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        AccessToken accessToken = tokenService.getAccessTokenByRefreshToken(tokenValue).orElse(null);
        if (accessToken == null) {
            return null;
        }
        return createOAuth2RefreshToken(accessToken);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        AccessToken accessToken = tokenService.getAccessTokenByRefreshToken(token.getValue()).orElse(null);
        if (accessToken == null) {
            return null;
        }
        return createOAuth2Authentication(accessToken);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        // 因为这里refreshToken是跟accesToken一起存的，所以删除refreshToken其实等价于删除accessToken.
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        tokenService.removeAccessTokenByRefreshToken(refreshToken.getValue());
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        // 每次登录都应该重新创建一个新的access token。
        return null;
//        if (authentication.isClientOnly()) {
//            return null;
//        }
//
//        Long userId = UserIdExtractor.getUserIdFromAuthentication(authentication);
//        if (userId == null) {
//            return null;
//        }
//        // 暂时只会有一个clientId
////            String clientKey = authentication.getOAuth2Request().getClientId();
//        List<AccessToken> accessTokens = tokenService.getAccessTokenByClientIdAndUserId(userId, Client.DEFAULT_CLIENT_ID);
//        if (accessTokens.isEmpty()) {
//            return null;
//        }
//
//        AccessToken accessToken = accessTokens.get(0);
//        return createOAuth2AccessToken(accessToken);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        return tokenService.getAccessTokenByClientIdAndUserId(Client.DEFAULT_CLIENT_ID, Long.parseLong(userName))
                .map((t) -> Collections.singletonList(createOAuth2AccessToken(t)))
                .orElseGet(() -> Collections.emptyList());
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        // return empty collection.
        return new ArrayList<>();
    }
}
