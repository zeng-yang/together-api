package com.zhlzzz.together.system.feedback;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="sys_feedback")
public class FeedbackEntity implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    private Long userId;

    @Getter @Setter
    @Column(length = 200)
    private String email;

    @Getter @Setter
    @Column(length = 200)
    private String content;

    @Getter @Setter
    @Column(nullable = false)
    private boolean finished = false;

    @Getter @Setter
    @Column
    private LocalDateTime createTime;
}
