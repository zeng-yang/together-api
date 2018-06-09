package com.zhlzzz.together.rank.player.player_season;

import javax.annotation.Nullable;

public class PlayerSeasonNotFoundException extends RuntimeException {

    private Long id;


    public PlayerSeasonNotFoundException(@Nullable Long id) {
        super(String.format("找不到排行榜信息（id: %d）", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
