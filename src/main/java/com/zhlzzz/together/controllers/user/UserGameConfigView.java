package com.zhlzzz.together.controllers.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.controllers.game.GameConfigView;
import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.user.user_game_config.UserGameConfigEntity;
import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApiModel(description = "用户游戏配置")
@JsonPropertyOrder({"id"})
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserGameConfigView {

    @NonNull
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
