package com.zhlzzz.together.game;

import lombok.Getter;

public class GameTypeNameUsedException extends RuntimeException {

    @Getter
    private String name;

    public GameTypeNameUsedException(String name) {
        this(name, null);
    }

    GameTypeNameUsedException(String name, Throwable cause) {
        super(name + " 已被使用", cause);
        this.name = name;
    }
}
