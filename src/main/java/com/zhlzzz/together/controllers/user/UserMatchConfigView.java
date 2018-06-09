package com.zhlzzz.together.controllers.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApiModel(description = "用户游戏匹配配置")
@JsonPropertyOrder({"id"})
@RequiredArgsConstructor
public class UserMatchConfigView {

    @NonNull
    private final UserMatchConfig userMatchConfig;


    @ApiModelProperty(value = "配置id", example = "1")
    public Long getGameConfigId() { return userMatchConfig.getGameConfigId(); }

    @ApiModelProperty(value = "选项id", example = "1")
    public Long getOptionId() { return userMatchConfig.getOptionId(); }
}
