package com.zhlzzz.together.article.discuss;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

public interface DiscussRepository extends Repository<DiscussEntity, Long> {

    DiscussEntity save(DiscussEntity discussEntity);
    Optional<DiscussEntity> findById(Long id);
    Set<DiscussEntity> findByArticleId(Long articleId);
    Optional<DiscussEntity> findByIdAndArticleIdAndUserId(Long id,Long articleId,Long userId);
}
