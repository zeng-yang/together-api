package com.zhlzzz.together.user.user_match_config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;

@ToString()
public class UserMatchConfigImpl implements UserMatchConfig {

    @Delegate(types = {UserMatchConfigEntity.class})
    @Getter @Setter
    private UserMatchConfigEntity userMatchConfigEntity;

    public UserMatchConfigImpl(UserMatchConfigEntity userMatchConfigEntity) {
        this.userMatchConfigEntity = userMatchConfigEntity;
    }

    public UserMatchConfigDto toDto() {
        UserMatchConfigDto dto = UserMatchConfigDto.builder()
                .gameConfigId(getGameConfigId())
                .optionId(getOptionId())
                .build();
        return dto;
    }
}


