package com.zhlzzz.together.system;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="sys_about")
public class AboutEntity implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    @Column
    private String company;

    @Getter @Setter
    @Column(length = 200)
    private String logo;

    @Getter @Setter
    @Column(columnDefinition = "TEXT")
    private String introduction;

}
