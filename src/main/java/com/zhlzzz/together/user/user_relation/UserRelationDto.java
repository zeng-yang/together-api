package com.zhlzzz.together.user.user_relation;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@ToString
public class UserRelationDto implements UserRelation, Serializable {

    private Long id;

    private Long userId;

    private Long toUserId;

    private String remark;

    private Relation relation;

    private LocalDateTime updateTime;
}
