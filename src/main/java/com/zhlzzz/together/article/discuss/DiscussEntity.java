package com.zhlzzz.together.article.discuss;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="discuss")
@ToString
public class DiscussEntity implements Discuss {

    @Getter
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private boolean audit = true;

    @Override
    public boolean isAudit() {
        return audit;
    }

    @Setter
    @Column(nullable = false)
    private boolean toTop = false;

    @Override
    public boolean isToTop() {
        return toTop;
    }

    @Getter @Setter
    @Column
    private  Long articleId;

    @Getter @Setter
    @Column
    private  Long userId;

    @Getter @Setter
    @Column(length = 500, nullable = false)
    private String content;

    @Getter @Setter
    @Column
    private LocalDateTime createTime;

    @Getter @Setter
    @Column
    private String replyContent;

    @Getter @Setter
    @Column
    private LocalDateTime replyTime;

    public DiscussDto toDto() {
        return DiscussDto.builder()
                .id(id)
                .audit(audit)
                .toTop(toTop)
                .articleId(articleId)
                .userId(userId)
                .content(content)
                .createTime(createTime)
                .replyContent(replyContent)
                .replyTime(replyTime)
                .build();
    }
}
