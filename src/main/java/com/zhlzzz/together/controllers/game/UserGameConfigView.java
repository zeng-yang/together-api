package com.zhlzzz.together.controllers.game;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.user.user_game_config.UserGameConfigEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApiModel(description = "用户游戏配置")
@JsonPropertyOrder({"id"})
@RequiredArgsConstructor
public class UserGameConfigView {

    private final UserGameConfigEntity userGameConfigEntity;

    @ApiModelProperty(value = "id", example = "1")
    public Long getId() { return userGameConfigEntity.getId(); }

    @ApiModelProperty(value = "用户id", example = "1")
    public Long getUserId() { return userGameConfigEntity.getUserId(); }

    @ApiModelProperty(value = "游戏分类id", example = "1")
    public Integer getGameTypeId() { return userGameConfigEntity.getGameTypeId(); }

    @ApiModelProperty(value = "用户游戏昵称", example = "1")
    public String getNickname() { return userGameConfigEntity.getNickname(); }

    @ApiModelProperty(value = "用户游戏配置沟通", example = "1")
    public String getContact() { return userGameConfigEntity.getContact(); }

    @ApiModelProperty(value = "用户游戏配置区域", example = "1")
    public String getArea() { return userGameConfigEntity.getArea(); }
}
