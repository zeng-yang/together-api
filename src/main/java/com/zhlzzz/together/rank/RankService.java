package com.zhlzzz.together.rank;

import java.util.List;

public interface RankService {

    List<RankEntity> findRankList(Long userId);
}
