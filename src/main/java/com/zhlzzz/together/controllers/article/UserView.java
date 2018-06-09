package com.zhlzzz.together.controllers.article;

import com.zhlzzz.together.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;

@ApiModel(description = "用户")
@RequiredArgsConstructor
public class UserView {

    private final User user;

    @ApiModelProperty(name = "用户ID",example = "1")
    public Long getId() { return user.getId(); }

    @ApiModelProperty(name = "用户头像",example = "http://www.baidu.com")
    public String getAvatarUrl() { return user.getAvatarUrl(); }

    @ApiModelProperty(name = "用户昵称",example = "小星星")
    public String getNickname() { return user.getNickName(); }

}
