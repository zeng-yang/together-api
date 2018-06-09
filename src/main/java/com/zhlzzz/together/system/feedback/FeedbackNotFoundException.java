package com.zhlzzz.together.system.feedback;

import lombok.Getter;

import javax.annotation.Nullable;

public class FeedbackNotFoundException extends RuntimeException {

    @Getter
    private Long id;

    public FeedbackNotFoundException(@Nullable Long id) {
        super(String.format("找不到相关意见反馈（id: %d）", id));
        this.id = id;
    }

}
