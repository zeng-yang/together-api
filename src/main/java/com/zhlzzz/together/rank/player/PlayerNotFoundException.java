package com.zhlzzz.together.rank.player;

import javax.annotation.Nullable;

public class PlayerNotFoundException extends RuntimeException {

    private Long id;


    public PlayerNotFoundException(@Nullable Long id) {
        super(String.format("找不到指定用户游戏内信息（id: %d）", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
