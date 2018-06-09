package com.zhlzzz.together.user;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@ToString
public class UserDto implements User, Serializable {

    private Long id;
    private String phone;
    private String openId;
    private String unionId;
    private Role role;
    private String nickName;
    private String avatarUrl;
    private String qRCode;
    private Integer gender;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private boolean admin;

}
