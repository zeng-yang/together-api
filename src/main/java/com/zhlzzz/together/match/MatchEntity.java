package com.zhlzzz.together.match;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "matche")
public class MatchEntity implements Match, Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private Long userId;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private Integer gameTypeId;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private String formId;

    @Setter(AccessLevel.PACKAGE)
    @Column
    private boolean onlyFriend = false;

    @Setter(AccessLevel.PACKAGE)
    @Column
    private boolean finished = false;

    @Setter(AccessLevel.PACKAGE)
    @Column
    private boolean deleted = false;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime createTime;

    @Getter @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime expiration;

    @Override
    public boolean isOnlyFriend() {
        return onlyFriend;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isDeleted() { return deleted; }

    @Override
    public boolean isEffective() {
        return !finished && !deleted && expiration.isAfter(LocalDateTime.now());
    }

    public MatchDto toDto() {
        return MatchDto.builder()
                .id(id)
                .userId(userId)
                .createTime(createTime)
                .expiration(expiration)
                .finished(finished)
                .deleted(deleted)
                .effective(isEffective())
                .build();
    }
}
