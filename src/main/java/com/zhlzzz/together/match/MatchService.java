package com.zhlzzz.together.match;

import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.game.GameType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MatchService {

    Match addMatch(Long userId, Integer gameTypeId, Long minute, String formId, Boolean onlyFriend);

    Boolean finish(Long id);

    Boolean delete(Long id);

    Optional<? extends Match> getCurrentMatchByUser(Long userId);

    Slice<? extends Match, Integer> getMatchs(SliceIndicator<Integer> indicator);

    List<? extends Match> getMatchsInUserIds(Set<Long> userIds);

    List<? extends Match> getMatchsByUserIdsInAndEffective(Set<Long> userIds, Integer gameTypeId);
}
