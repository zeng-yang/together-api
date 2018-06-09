package com.zhlzzz.together.game.game_config;

import java.util.Optional;

public interface GameConfigService {

    GameConfigEntity addGameConfig(Integer gameTypeId, GameConfig.InputType inputType, String label, Boolean required);

    GameConfigEntity updateGameConfig(Long id, GameConfig.InputType inputType, String label, Boolean required);

    Optional<GameConfigEntity> getGameConfigById(Long id);

    GameConfigOptionEntity addOption(Long configId, String value);

    GameConfigOptionEntity updateOption(Long id, String value);

    Optional<GameConfigOptionEntity> getGameConfigOptionById(Long id);

    void deleteGameConfig(Long id);

    void deleteOption(Long id);


}
