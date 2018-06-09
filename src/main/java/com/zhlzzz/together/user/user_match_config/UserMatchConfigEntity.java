package com.zhlzzz.together.user.user_match_config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_match_config",indexes = {
        @Index(name = "user_game_config_idx", columnList = "userGameConfigId")
})
public class UserMatchConfigEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    private Long userGameConfigId;

    @Getter @Setter
    @Column
    private Long gameConfigId;

    @Getter @Setter
    @Column
    private Long optionId;
}
