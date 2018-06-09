package com.zhlzzz.together.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParam implements Serializable {

    protected String phone;

    protected String openId;

    protected String unionId;

    protected String nickName;

    protected String avatarUrl;

    protected Integer gender;

    protected User.Role role;

    protected String qRCode;

}
