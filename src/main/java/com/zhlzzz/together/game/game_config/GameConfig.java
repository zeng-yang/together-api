package com.zhlzzz.together.game.game_config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

public interface GameConfig extends Serializable {

    enum InputType {
        radio, checkbox
    }

    interface Option {
        Long getId();
        String getValue();
    }

    @Nonnull
    Long getId();

    @Nullable
    String getLabel();

    InputType getInputType();

    boolean isRequired();

    List<? extends Option> getOptions();

}
