package com.zhlzzz.together.article.discuss;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(description = "评论参数")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyParam implements Serializable {

    @ApiModelProperty(value = "回复内容", example = "回复")
    protected String replyContent;

}
