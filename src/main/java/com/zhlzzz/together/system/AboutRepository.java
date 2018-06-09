package com.zhlzzz.together.system;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AboutRepository extends Repository<AboutEntity, Long> {

    AboutEntity save(AboutEntity systemEntity);
    Optional<AboutEntity> findById(Integer id);
}
