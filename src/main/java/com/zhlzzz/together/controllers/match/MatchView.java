package com.zhlzzz.together.controllers.match;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.match.Match;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(description = "游戏匹配")
@JsonPropertyOrder({"id"})
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class MatchView {

    private final Match match;

    @ApiModelProperty(value = "匹配Id", example = "1")
    public Long getId() { return match.getId(); }

    @ApiModelProperty(value = "匹配用户Id", example = "1")
    public Long getUserId() { return match.getUserId(); }

    @ApiModelProperty(value = "匹配游戏种类Id", example = "1")
    public Integer getGameTypeId() { return match.getGameTypeId(); }

    @ApiModelProperty(value = "是否仅好友", example = "true")
    public boolean isOnlyFriend() { return match.isOnlyFriend(); }

    @ApiModelProperty(value = "是否已完成", example = "true")
    public boolean isFinished() { return match.isFinished(); }

    @ApiModelProperty(value = "是否已取消", example = "true")
    public boolean isDeleted() { return match.isDeleted(); }

    @ApiModelProperty(value = "是否有效", example = "true")
    public boolean isEffective() { return match.isEffective(); }

    @ApiModelProperty(name="创建时间",example = "2018-06-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0800")
    public LocalDateTime getCreateTime() { return match.getCreateTime(); }

    @ApiModelProperty(name="过期时间",example = "2018-06-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0800")
    public LocalDateTime getExpiration() { return match.getExpiration(); }
}
