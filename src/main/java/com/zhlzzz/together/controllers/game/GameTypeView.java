package com.zhlzzz.together.controllers.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.controllers.user.UserMatchConfigView;
import com.zhlzzz.together.game.GameType;
import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.user.user_game_config.UserGameConfigEntity;
import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "游戏类别")
@JsonPropertyOrder({"id","hot","name","imgUrl","deleted","createTime","userGameConfigs","matchConfigs","userMatchConfigs"})
@RequiredArgsConstructor
public class GameTypeView {

    @NonNull
    private final GameType gameType;
    private final Boolean isShow;
    private final List<? extends GameConfig> gameConfigs;
    private final UserGameConfigEntity userGameConfigEntity;
    private List<GameConfigView> gameConfigViews;
    private final List<? extends UserMatchConfig> userMatchConfigs;
    private List<UserMatchConfigView> userMatchConfigViews;
    private UserGameConfigView userGameConfigView;

    @ApiModelProperty(value = "游戏ID", example = "1")
    public Integer getId() { return gameType.getId(); }

    @ApiModelProperty(value = "游戏名称", example = "绝地求生")
    public String getName() { return gameType.getName(); }

    @ApiModelProperty(value = "背景图", example = "http://www.baidu.com")
    public String getImgUrl() { return gameType.getImgUrl(); }

    @ApiModelProperty(value = "是否标注", example = "true")
    public Boolean isHot() { return gameType.isHot(); }

    @ApiModelProperty(value = "是否删除", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean isDeleted() {
        if (isShow) {
            return gameType.isDeleted();
        } else {
            return null;
        }
    }

    @ApiModelProperty(value = "创建时间", example = "2017-12-13 12:03:20")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0800")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDateTime getCreateTime() {
        if (isShow) {
            return gameType.getCreateTime();
        } else {
            return null;
        }
    }

    @ApiModelProperty(value = "游戏基本配置项列表")
    public List<GameConfigView> getMatchConfigs() {
        if (gameConfigs == null) {
            return null;
        }
        if (gameConfigViews == null) {
            gameConfigViews = CollectionUtils.map(gameConfigs,GameConfigView::new);
        }
        return gameConfigViews;
    }

    @ApiModelProperty(value = "用户游戏基本配置列表")
    public UserGameConfigView getUserGameConfigs() {
        if (userGameConfigEntity == null) {
            return null;
        }
        if (userGameConfigView == null) {
            userGameConfigView = new UserGameConfigView(userGameConfigEntity);
        }
        return userGameConfigView;
    }

    @ApiModelProperty(value = "用户游戏匹配项配置值列表")
    public List<UserMatchConfigView> getUserMatchConfigs() {
        if (userMatchConfigs == null) {
            return new ArrayList<>();
        }
        if (userMatchConfigViews == null) {
            userMatchConfigViews = CollectionUtils.map(userMatchConfigs, UserMatchConfigView::new);
        }
        return userMatchConfigViews;
    }
}
