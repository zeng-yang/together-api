package com.zhlzzz.together.match;

import javax.annotation.Nullable;

public class MatchNotFoundException extends RuntimeException {
    private Long id;


    public MatchNotFoundException(@Nullable Long id) {
        super(String.format("找不到指定匹配记录（id: %d）", id));
        this.id = id;
    }


    public Long getId() {
        return id;
    }

}
