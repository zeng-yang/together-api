package com.zhlzzz.together.user.user_relation;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface UserRelation extends Serializable {
    enum Relation {
        friend, nofriend
    }

    Long getId();
    Long getUserId();
    Long getToUserId();
    String getRemark();
    Relation getRelation();
    LocalDateTime getUpdateTime();
}
