package com.zhlzzz.together.controllers.game;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApiModel(description = "游戏配置字段")
@JsonPropertyOrder({"id","label","inputType","required","options"})
@RequiredArgsConstructor
public class GameConfigView {

    @ApiModel(description = "选项")
    @RequiredArgsConstructor
    @JsonPropertyOrder({"id", "value"})
    public static class Option {

        private final GameConfig.Option option;

        @ApiModelProperty(value = "选项id", example = "2")
        public Long getId() { return option.getId(); }

        @ApiModelProperty(value = "选项值", example = "abc")
        public String getValue() { return option.getValue(); }

    }

    private final GameConfig gameConfig;
    private List<Option> options;

    public Long getId() { return gameConfig.getId(); }

    public String getLabel() { return gameConfig.getLabel(); }

    public GameConfig.InputType getInputType() { return gameConfig.getInputType(); }

    public boolean isRequired() { return gameConfig.isRequired(); }

    public List<Option> getOptions() {
        if (gameConfig.getOptions() == null) {
            return null;
        }

        if (options == null) {
            options = CollectionUtils.map(gameConfig.getOptions(), GameConfigView.Option::new);
        }

        return options;
    }
}
