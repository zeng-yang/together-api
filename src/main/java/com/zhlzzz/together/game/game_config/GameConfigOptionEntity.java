package com.zhlzzz.together.game.game_config;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "game_config_options", indexes = {
        @Index(name = "config_idx", columnList = "configId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameConfigOptionEntity implements GameConfig.Option, Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    private Long configId;

    @Getter @Setter
    @Column
    private String value;
}
