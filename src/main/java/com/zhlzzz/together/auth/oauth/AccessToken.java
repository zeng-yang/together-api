package com.zhlzzz.together.auth.oauth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "auth_access_token", uniqueConstraints = {
        @UniqueConstraint(name = "token_udx", columnNames = {"token"}),
        @UniqueConstraint(name = "user_client_udx", columnNames = {"userId", "clientId"})
})
@NamedNativeQueries({
        @NamedNativeQuery(name = AccessToken.ADD_QUERY_NAME, query = "INSERT auth_access_token(token, user_id, client_id, create_time, expiration, grant_type, scope, refresh_token, refresh_token_create_time, refresh_token_expiration) VALUES(:token, :userId, :clientId, :createTime, :expiration, :grantType, :scope, :refreshToken, :refreshTokenCreateTime, :refreshTokenExpiration) ON DUPLICATE KEY UPDATE token = VALUES(token), create_time = VALUES(create_time), expiration = VALUES(expiration), grant_type = VALUES(grant_type), scope = VALUES(scope), refresh_token = VALUES(refresh_token), refresh_token_create_time = VALUES(refresh_token_create_time), refresh_token_expiration = VALUES(refresh_token_expiration)")
})
public class AccessToken {

    static final String ADD_QUERY_NAME = "auth.oauth.add_access_token";

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 64)
    private String token;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    @Nullable
    private Long userId;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private Long clientId;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime createTime;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    @Nullable
    private LocalDateTime expiration;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 20)
    private String grantType;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 200)
    @Nullable
    @Convert(converter = ScopeStringConverter.class)
    private Set<String> scope;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 64)
    @Nullable
    private String refreshToken;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    @Nullable
    private LocalDateTime refreshTokenCreateTime;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    @Nullable
    private LocalDateTime refreshTokenExpiration;


}
