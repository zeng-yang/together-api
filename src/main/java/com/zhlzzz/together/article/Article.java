package com.zhlzzz.together.article;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface Article extends Serializable {

    Long getId();
    String getTitle();
    String getAuthor();
    String getIntroduction();
    String getImgUrl();
    String getContent();
    LocalDateTime getCreateTime();
}
