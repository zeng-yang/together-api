package com.zhlzzz.together.controllers.rank;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.rank.RankEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;

@ApiModel(description = "排行榜")
@JsonPropertyOrder({"palyerId","shardId","palyerName","userId"})
public class RankView {

    @NonNull
    private final RankEntity rankEntity;

    public RankView(RankEntity rankEntity){
        this.rankEntity = rankEntity;
    }

    @ApiModelProperty(name="用户ID",example = "123")
    public Long getUserId(){
        return this.rankEntity.getUserId();
    }

    @ApiModelProperty(name="Rating",example = "false")
    public Double getRating(){
        return this.rankEntity.getRating();
    }

    @ApiModelProperty(name="KD",example = "false")
    public Double getKd(){
        return this.rankEntity.getKd();
    }

    @ApiModelProperty(name = "游戏内昵称",example = "shroud")
    public String getPalyerName(){
        return this.rankEntity.getPlayerName();
    }

}
