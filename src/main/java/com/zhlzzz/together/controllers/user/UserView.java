package com.zhlzzz.together.controllers.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.user_label.UserLabelEntity;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApiModel(description = "用户")
@JsonPropertyOrder({"id","openId","nickName","avatarUrl","gender"})
@RequiredArgsConstructor
public class UserView {

    @NonNull
    private final User user;
    private final List<UserLabelEntity> userLabelEntitys;
    private List<UserLabelView> userLabelViews;

    @ApiModelProperty(value = "用户id", example = "1")
    public Long getId() { return user.getId(); }

    @ApiModelProperty(value = "用户昵称", example = "小星星")
    public String getNickName() { return user.getNickName(); }

    @ApiModelProperty(value = "用户头像url", example = "http://www.wechat.com")
    public String getAvatarUrl() { return user.getAvatarUrl(); }

    @ApiModelProperty(value = "用户性别", example = "1")
    public Integer getGender() { return user.getGender(); }

    @ApiModelProperty(value = "用户标签")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<UserLabelView> getLabels() {
        if (userLabelEntitys == null) {
            return null;
        }
        if (userLabelViews == null) {
            userLabelViews = CollectionUtils.map(userLabelEntitys, UserLabelView::new);
        }
        return userLabelViews;
    }
}
