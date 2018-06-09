package com.zhlzzz.together.auth.password;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth_passwords")
@NamedNativeQueries({
        @NamedNativeQuery(name = PasswordEntity.UPDATE_QUERY_NAME, query = "INSERT INTO auth_passwords(user_id, hashed_password, update_time) VALUES(:userId,:hashedPassword,:updateTime) ON DUPLICATE KEY UPDATE hashed_password = VALUES(hashed_password), update_time = VALUES(update_time)")
})
class PasswordEntity implements Serializable {

    public static final String UPDATE_QUERY_NAME = "auth.password.update";

    private static final long serialVersionUID = -1857712822315436294L;

    @Getter
    @Setter
    @Id
    private Long userId;

    @Getter @Setter
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Getter @Setter
    @Column(length = 128, columnDefinition = "VARCHAR(128) COLLATE utf8_bin", nullable = false)
    private String hashedPassword;

}
