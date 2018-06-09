package com.zhlzzz.together.user.user_match_config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

public interface UserMatchConfig extends Serializable {
    @Nonnull
    Long getGameConfigId();
    @Nullable
    Long getOptionId();
}
