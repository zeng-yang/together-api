package com.zhlzzz.together.system.feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    FeedbackEntity addFeedback(Long userId, FeedbackParam aboutParam);
    void finishedFeedback(Long id);
    Optional<FeedbackEntity> findFeedbackParamById(Long id);
    List<FeedbackEntity> findAll();
}
