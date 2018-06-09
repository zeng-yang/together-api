package com.zhlzzz.together.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "openId_udx", columnNames = {"openId"})
})
public class UserEntity implements User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 20)
    private String phone;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private String openId;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private String unionId;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private Role role;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 50)
    private String nickName;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 200)
    private String avatarUrl;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column(length = 200)
    private String qRCode;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private Integer gender;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime createTime;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime lastLoginTime;

    @Override
    public boolean isAdmin() { return this.role == Role.admin; }

    public UserDto toDto() {
        return UserDto.builder()
                .id(id)
                .phone(phone)
                .openId(openId)
                .unionId(unionId)
                .gender(gender)
                .avatarUrl(avatarUrl)
                .qRCode(qRCode)
                .avatarUrl(avatarUrl)
                .lastLoginTime(lastLoginTime)
                .createTime(createTime)
                .admin(isAdmin())
                .build();
    }
}
