package com.zhlzzz.together.user.user_label;

import lombok.Getter;

public class UserLabelUsedException extends RuntimeException {

    @Getter
    private String label;

    public UserLabelUsedException(String label) {
        super(label + "已经存在");
        this.label = label;
    }
}
