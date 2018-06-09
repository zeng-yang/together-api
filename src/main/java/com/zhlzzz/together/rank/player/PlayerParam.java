package com.zhlzzz.together.rank.player;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(description = "用户游戏内参数")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerParam implements Serializable {

    @ApiModelProperty(value = "游戏内昵称", example = "shroud")
    protected String palyerName;

}
