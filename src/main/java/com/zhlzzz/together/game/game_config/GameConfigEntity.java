package com.zhlzzz.together.game.game_config;

import com.zhlzzz.together.game.game_config.GameConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "game_config")
public class GameConfigEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    private Integer gameTypeId;

    @Getter @Setter
    @Column
    private GameConfig.InputType inputType;

    @Getter @Setter
    @Column(length = 50)
    private String label;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private boolean required = true;

}
