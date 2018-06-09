package com.zhlzzz.together.article;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@ToString
public class ArticleDto implements Article, Serializable {

    @NonNull
    private Long id;

    private String title;

    private String author;

    private String introduction;

    private String imgUrl;

    private String content;

    private LocalDateTime createTime;
}
