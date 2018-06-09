package com.zhlzzz.together.rank.player.player_season;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "player_season")
public class PlayerSeasonEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private Long userId;

    @Getter @Setter
    private String seasonId;

    @Getter @Setter
    private Double kd;

    @Getter @Setter
    private Double winPoints;

    @Getter @Setter
    private Integer kills;

    @Getter @Setter
    private Integer losses;

    @Getter @Setter
    private Integer wins;

    @Getter @Setter
    private String type;

    @Getter @Setter
    private Integer assists;

    @Getter @Setter
    private Integer boosts;

    @Getter @Setter
    private Integer dbnos;

    @Getter @Setter
    private Integer dailyKills;

    @Getter @Setter
    private Double damageDealt;

    @Getter @Setter
    private Integer days;

    @Getter @Setter
    private Integer headshotKills;

    @Getter @Setter
    private Integer heals;

    @Getter @Setter
    private Double killPoints;

    @Getter @Setter
    private Double longestKill;

    @Getter @Setter
    private Double longestTimeSurvived;

    @Getter @Setter
    private Integer maxKillStreaks;

    @Getter @Setter
    private Double mostSurvivalTime;

    @Getter @Setter
    private Integer revives;

    @Getter @Setter
    private Double rideDistance;

    @Getter @Setter
    private Integer roadKills;

    @Getter @Setter
    private Integer roundMostKills;

    @Getter @Setter
    private Integer roundsPlayed;

    @Getter @Setter
    private Integer suicides;

    @Getter @Setter
    private Integer teamKills;

    @Getter @Setter
    private Double timeSurvived;

    @Getter @Setter
    private Integer top10s;

    @Getter @Setter
    private Integer vehicleDestroys;

    @Getter @Setter
    private Double walkDistance;

    @Getter @Setter
    private Integer weaponsAcquired;

    @Getter @Setter
    private Integer weeklyKills;

}