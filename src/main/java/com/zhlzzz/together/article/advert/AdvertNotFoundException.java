package com.zhlzzz.together.article.advert;

import lombok.Getter;

import javax.validation.constraints.NotNull;

public class AdvertNotFoundException extends RuntimeException {

    @Getter
    private Long id;

    public AdvertNotFoundException(@NotNull Long id) {
        super(String.format("找不到指定的广告图（id: %d）", id));
        this.id = id;
    }
}
