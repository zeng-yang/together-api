package com.zhlzzz.together.controllers.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.user.user_label.UserLabelEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;

@ApiModel(description = "用户标签")
@JsonPropertyOrder({"id","label","userId"})
public class UserLabelView {

    @NonNull
    private final UserLabelEntity userLable;

    public UserLabelView(UserLabelEntity userLable) {
        this.userLable = userLable;
    }

    @ApiModelProperty(value = "标签id", example = "1")
    public Long getId() { return userLable.getId(); }

    @ApiModelProperty(value = "用户标签", example = "帅哥")
    public String getLabel() { return userLable.getLabel(); }

    @ApiModelProperty(value = "是否展示", example = "true")
    public Boolean isShowed() { return userLable.isShowed(); }
}
