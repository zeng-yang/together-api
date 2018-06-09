package com.zhlzzz.together.rank.player.player_season;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface PlayerSeasonRepository extends Repository<PlayerSeasonEntity, Long> {

    Optional<PlayerSeasonEntity> findByUserIdAndSeasonId(Long userId, String seasonId);
}
