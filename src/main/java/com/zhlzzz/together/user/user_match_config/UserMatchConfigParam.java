package com.zhlzzz.together.user.user_match_config;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMatchConfigParam implements Serializable {

    @NonNull
    private Long gameConfigId;

    private Long optionId;
}
