package com.zhlzzz.together.game.game_config;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
public class GameConfigDto implements GameConfig {

    @Value
    @Builder
    public static class Option implements GameConfig.Option {
        private Long id;
        private String value;
    }

    @NonNull
    private Long id;

    private String label;

    private InputType inputType;

    private List<Option> options;

    private boolean required;
}
