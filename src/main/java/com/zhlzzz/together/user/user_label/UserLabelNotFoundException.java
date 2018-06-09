package com.zhlzzz.together.user.user_label;

import lombok.Getter;

import javax.validation.constraints.NotNull;

public class UserLabelNotFoundException extends RuntimeException {

    @Getter
    private Long id;

    public UserLabelNotFoundException(@NotNull Long id) {
        super(String.format("找不到指定的标签（id: %d）", id));
        this.id = id;
    }
}
