package com.zhlzzz.together.article.discuss;

import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;

import java.util.Optional;
import java.util.Set;

public interface DiscussService {

    Discuss addDiscuss(Long articleId, Long userId, String content);
    Discuss updateDiscuss(Long id, DiscussParam param);

    Optional<? extends  Discuss> getDiscussById(Long id);

    void deleteDiscuss(Long id);

    Slice<? extends Discuss, Integer> getDiscussesByCriteria(DiscussCriteria criteria, SliceIndicator<Integer> indicator);

}
