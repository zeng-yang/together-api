package com.zhlzzz.together.user.user_match_config;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class UserMatchConfigDto implements UserMatchConfig, Serializable {
    @NonNull
    private Long gameConfigId;
    private Long optionId;
}
