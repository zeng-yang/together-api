package com.zhlzzz.together.controllers.system;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.system.feedback.FeedbackEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;

@ApiModel(description = "反馈意见")
@JsonPropertyOrder({"id","userId","email","content"})
public class FeedbackView {

    @NonNull
    private final FeedbackEntity feedbackEntity;

    public FeedbackView(FeedbackEntity feedbackEntity) {
         this.feedbackEntity = feedbackEntity;
    }

    @ApiModelProperty(name = "反馈ID",example = "1")
    public  Long getId(){
        return feedbackEntity.getId();
    }

    @ApiModelProperty(value = "反馈用户id", example = "123")
    public Long getUserId() { return feedbackEntity.getUserId(); }

    @ApiModelProperty(value = "用户邮箱", example = "1234567890@qq.com")
    public String getEmail() { return feedbackEntity.getEmail(); }

    @ApiModelProperty(value = "反馈内容", example = "反馈反馈反馈")
    public String getContent() { return feedbackEntity.getContent(); }
}
