package com.zhlzzz.together.controllers.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.article.discuss.Discuss;
import com.zhlzzz.together.article.discuss.DiscussEntity;
import com.zhlzzz.together.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(description = "评论")
@JsonPropertyOrder({"id","audit","toTop","articleId","content","createTime"})
@RequiredArgsConstructor
public class DiscussView {

    @NonNull
    private final Discuss discuss;
    private final User user;
    private UserView userView;


    @ApiModelProperty(name = "评论ID",example = "1")
    public  Long getId(){
        return discuss.getId();
    }

    @ApiModelProperty(name ="是否审核",example = "true")
    public Boolean getAudit(){
        return discuss.isAudit();
    }

    @ApiModelProperty(name ="是否置顶",example = "true")
    public Boolean getToTop(){
        return discuss.isToTop();
    }

    @ApiModelProperty(name ="文章id",example = "123")
    public Long getArticleId(){
        return discuss.getArticleId();
    }

    @ApiModelProperty(name="评论内容",example = "评论")
    public String getContent(){
        return discuss.getContent();
    }

    @ApiModelProperty(name = "评论时间",example = "2017-12-13 12:03:20")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0800")
    public LocalDateTime getCreateTime(){
        return discuss.getCreateTime();
    }

    @ApiModelProperty(name="官方回复内容",example = "回复")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getReplyContent(){
        return discuss.getReplyContent();
    }

    @ApiModelProperty(name="官方回复时间",example = "回复")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0800")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDateTime getReplyTime() { return discuss.getReplyTime(); }

    @ApiModelProperty(value = "用户", dataType = "UserView")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object getUser() {
        if (user == null) {
            return null;
        }
        if (userView == null) {
            userView = new UserView(user);
        }
        return userView;
    }



}
