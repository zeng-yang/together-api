package com.zhlzzz.together.rank.player;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends Repository<PlayerEntity, Long> {

    PlayerEntity save(PlayerEntity playerEntity);
    Optional<PlayerEntity> findByUserId(Long userId);
    Set<PlayerEntity> findAll();
}
