package com.zhlzzz.together.user.user_game_config;

import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import com.zhlzzz.together.user.user_match_config.UserMatchConfigParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserGameConfigService {

    UserGameConfigEntity updateUserGameConfig(Long userId, Integer gameTypeId, UserGameConfigParam userGameConfigParam);

    Optional<UserGameConfigEntity> getUserGameConfigByUserAndGameType(Long userId, Integer gameTypeId);

    Set<UserGameConfigEntity> getUserGameConfigsByGameType(Integer gameTypeId);

    List<? extends UserMatchConfig> updateUserMatchConfig(Long userGameConfigId, List<UserMatchConfigParam> params);

    List<? extends UserMatchConfig> getUserMatchConfigByUserGameConfigId(Long userGameConfigId);
}
