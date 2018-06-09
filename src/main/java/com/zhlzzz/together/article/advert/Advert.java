package com.zhlzzz.together.article.advert;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface Advert extends Serializable {

    Long getId();
    Long getArticleId();
    String getTitle();
    String getAdvertUrl();
    LocalDateTime getCreateTime();
}
