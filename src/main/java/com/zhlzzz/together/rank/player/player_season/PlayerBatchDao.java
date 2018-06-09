package com.zhlzzz.together.rank.player.player_season;

import java.util.List;

public interface PlayerBatchDao<T> {
    public void batchInsert(List<T> list);
    public void batchUpdate(List<T> list);
}
