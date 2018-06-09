package com.zhlzzz.together.article.advert;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="advert")
public class AdvertEntity implements Advert {

    @Getter
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    private  Long articleId;

    @Getter @Setter
    @Column(length = 100)
    private String title;

    @Getter @Setter
    @Column(length = 200)
    private String advertUrl;


    @Getter @Setter
    @Column
    private LocalDateTime createTime;


}
