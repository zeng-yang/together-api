package com.zhlzzz.together.article.discuss;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
public class DiscussDto implements Discuss, Serializable {

    private Long id;

    private boolean audit;

    private boolean toTop;

    private Long articleId;

    private Long userId;

    private String content;

    private String replyContent;

    private LocalDateTime createTime;

    private LocalDateTime replyTime;
}
