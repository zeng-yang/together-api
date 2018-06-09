package com.zhlzzz.together.game;

import com.zhlzzz.together.game.game_config.GameConfig;

import java.util.List;
import java.util.Optional;

public interface GameTypeService {
    GameType addGameType(String name, String imgUrl, Boolean hot) throws GameTypeNameUsedException;

    GameType updateGameType(Integer id, String name, String imgUrl, Boolean hot, Boolean deleted) throws GameTypeNotFoundException, GameTypeNameUsedException;

    Optional<? extends GameType> getGameTypeById(Integer id);

    List<? extends GameType> getAllGameTypes();

    List<? extends GameConfig> getGameTypeConfigs(Integer gameTypeId);
}
