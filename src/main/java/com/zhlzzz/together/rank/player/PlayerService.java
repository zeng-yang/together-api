package com.zhlzzz.together.rank.player;

import org.springframework.core.env.Environment;

public interface PlayerService {

    PlayerEntity savePlayer(Long userId, String playerName, String shardId, Environment evn) ;
}
