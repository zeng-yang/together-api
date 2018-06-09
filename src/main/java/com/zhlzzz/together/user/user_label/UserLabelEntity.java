package com.zhlzzz.together.user.user_label;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_label")
public class UserLabelEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    private Long userId;

    @Getter @Setter
    @Column(length = 10, nullable = false)
    private String label;

    @Getter @Setter
    @Column
    private boolean showed = false;
}
