package com.zhlzzz.together.rank.player;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlayerEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private String playerId;

    @Getter @Setter
    private String shardId;

    @Getter @Setter
    private String playerName;

    @Getter @Setter
    private String currentSeason;

    @Getter @Setter
    private Long userId;
}
