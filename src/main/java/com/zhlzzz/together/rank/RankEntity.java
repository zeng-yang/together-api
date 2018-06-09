package com.zhlzzz.together.rank;

import lombok.Getter;
import lombok.Setter;

public class RankEntity {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private Long userId;

    @Getter @Setter
    private String playerName;

    @Getter @Setter
    private Double rating;

    @Getter @Setter
    private Double kd;

}
