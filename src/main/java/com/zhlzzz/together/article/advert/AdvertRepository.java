package com.zhlzzz.together.article.advert;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AdvertRepository extends Repository<AdvertEntity, Long> {

    AdvertEntity save(AdvertEntity advertEntity);
    Optional<AdvertEntity> findById(Long id);
    List<AdvertEntity> findAll();
}
