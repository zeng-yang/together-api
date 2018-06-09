package com.zhlzzz.together.game;

import javax.annotation.Nullable;

public class GameTypeNotFoundException extends RuntimeException {

    private Integer id;

    private String name;

    public GameTypeNotFoundException(@Nullable Integer id) {
        super(String.format("找不到指定的游戏类型（id: %d）", id));
        this.id = id;
    }

    public GameTypeNotFoundException(@Nullable String name) {
        super(String.format("找不到指定的游戏类型（名字: %s）", name));
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
