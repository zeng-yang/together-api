package com.zhlzzz.together.game;

import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.game.game_config.GameConfigDto;
import com.zhlzzz.together.game.game_config.GameConfigEntity;
import com.zhlzzz.together.game.game_config.GameConfigOptionEntity;
import com.zhlzzz.together.utils.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.List;

@ToString
public class GameConfigImpl implements GameConfig {

    @Delegate(types = {GameConfigEntity.class})
    @Getter @Setter
    private GameConfigEntity gameConfigEntity;

    @Getter @Setter
    private List<GameConfigOptionEntity> options;

    public GameConfigImpl(GameConfigEntity gameConfigEntity, List<GameConfigOptionEntity> options) {
        this.gameConfigEntity = gameConfigEntity;
        this.options = options;
    }

    public GameConfigDto toDto() {
        GameConfigDto dto = GameConfigDto.builder()
                .id(getId())
                .label(getLabel())
                .inputType(getInputType())
                .required(isRequired())
                .options(CollectionUtils.map(options, (o)-> GameConfigDto.Option.builder().id(o.getId()).value(o.getValue()).build()))
                .build();
        return dto;
    }

}
