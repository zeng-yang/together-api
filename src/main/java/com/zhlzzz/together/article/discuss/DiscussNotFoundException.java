package com.zhlzzz.together.article.discuss;

import lombok.Getter;

import javax.annotation.Nullable;

public class DiscussNotFoundException extends RuntimeException {

    @Getter
    private Long id;

    public DiscussNotFoundException(@Nullable Long id) {
        super(String.format("找不到指定的评论（id: %d）", id));
        this.id = id;
    }

}
