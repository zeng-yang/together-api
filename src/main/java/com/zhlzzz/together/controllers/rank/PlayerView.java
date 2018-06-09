package com.zhlzzz.together.controllers.rank;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.rank.player.PlayerEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;

@ApiModel(description = "用户游戏信息")
@JsonPropertyOrder({"palyerId","shardId","palyerName","userId"})
public class PlayerView {

    @NonNull
    private final PlayerEntity playerEntity;

    public PlayerView(PlayerEntity playerEntity){
        this.playerEntity = playerEntity;
    }

    @ApiModelProperty(name="游戏内用户ID",example = "account.d50fdc18fcad49c691d38466bed6f8fd")
    public String getPalyerId(){
        return this.playerEntity.getPlayerId();
    }

    @ApiModelProperty(name="大区ID",example = "false")
    public String getShardId(){
        return this.playerEntity.getShardId();
    }

    @ApiModelProperty(name = "游戏内昵称",example = "shroud")
    public String getPalyerName(){
        return this.playerEntity.getPlayerName();
    }

    @ApiModelProperty(name ="用户ID",example = "123")
    public Long getUserId(){
        return this.playerEntity.getUserId();
    }
}
