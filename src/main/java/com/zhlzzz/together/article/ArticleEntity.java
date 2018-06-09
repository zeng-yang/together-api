package com.zhlzzz.together.article;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
public class ArticleEntity implements Article {

    private static final long serialVersionUID = 762181683977875924L;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column(length = 50, nullable = false)
    private String title;

    @Getter @Setter
    @Column(length = 30)
    private String author;

    @Getter @Setter
    @Column(length = 50)
    private String introduction;

    @Getter @Setter
    @Column
    private String imgUrl;

    @Getter @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Getter @Setter
    @Column
    private LocalDateTime createTime;

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .id(id)
                .author(author)
                .introduction(introduction)
                .title(title)
                .imgUrl(imgUrl)
                .content(content)
                .createTime(createTime)
                .build();
    }
}
