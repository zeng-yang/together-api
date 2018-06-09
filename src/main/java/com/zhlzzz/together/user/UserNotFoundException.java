package com.zhlzzz.together.user;

import javax.annotation.Nullable;

public class UserNotFoundException extends RuntimeException {

    private Long id;

    private String openId;

    public UserNotFoundException(@Nullable Long id) {
        super(String.format("找不到指定用户（id: %d）", id));
        this.id = id;
    }

    public UserNotFoundException(@Nullable String openId) {
        super(String.format("找不到指定用户（id: %d）", openId));
        this.openId = openId;
    }

    public Long getId() {
        return id;
    }

    public String getOpenId() {
        return openId;
    }
}
