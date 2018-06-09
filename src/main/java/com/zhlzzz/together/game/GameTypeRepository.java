package com.zhlzzz.together.game;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface GameTypeRepository extends Repository<GameTypeEntity, Integer> {
    GameTypeEntity save(GameTypeEntity gameTypeEntity);
    Optional<GameTypeEntity> getById(Integer id);
    List<GameTypeEntity> findAll();
}
