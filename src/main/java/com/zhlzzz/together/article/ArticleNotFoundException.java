package com.zhlzzz.together.article;

import lombok.Getter;

import javax.annotation.Nullable;

public class ArticleNotFoundException extends RuntimeException {

    @Getter
    private Long id;

    public ArticleNotFoundException(@Nullable Long id) {
        super(String.format("找不到指定的文章（id: %d）", id));
        this.id = id;
    }

}
