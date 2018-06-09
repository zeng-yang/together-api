package com.zhlzzz.together.user;

import lombok.Getter;

public class OpenIdUsedException extends RuntimeException {

    @Getter
    private String openId;

    public OpenIdUsedException(String openId, Throwable cause) {
        super(openId + " 已经存在", cause);
        this.openId = openId;
    }
}
