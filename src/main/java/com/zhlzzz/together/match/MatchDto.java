package com.zhlzzz.together.match;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@ToString
public class MatchDto implements Match, Serializable {

    @NonNull
    private Long id;
    private Long userId;
    private Integer gameTypeId;
    private String formId;
    private LocalDateTime createTime;
    private LocalDateTime expiration;
    private boolean onlyFriend;
    private boolean finished;
    private boolean deleted;
    private boolean effective;
}
