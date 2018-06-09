package com.zhlzzz.together.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_type", uniqueConstraints = {
        @UniqueConstraint(name = "name_udx", columnNames = {"name"})
})
public class GameTypeEntity implements GameType, Serializable {

    private static final long serialVersionUID = -2206493122498197563L;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    @Column(length = 20)
    private String name;

    @Getter @Setter
    @Column(length = 200)
    private String imgUrl;

    @Setter
    @Column(nullable = false)
    private boolean hot = false;

    @Setter
    @Column(nullable = false)
    private boolean deleted = false;

    @Getter @Setter
    @Column
    private LocalDateTime createTime;

    @Override
    public Boolean isHot() {
        return hot;
    }

    @Override
    public Boolean isDeleted() {
        return deleted;
    }
}
