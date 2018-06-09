package com.zhlzzz.together.system.feedback;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends Repository<FeedbackEntity, Long> {

    FeedbackEntity save(FeedbackEntity feedbackEntity);
    Optional<FeedbackEntity> findById(Long id);
    List<FeedbackEntity> findAll();
}
