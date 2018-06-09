package com.zhlzzz.together.system.feedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import java.io.Serializable;

@ApiModel(description = "反馈意见参数")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackParam implements Serializable {


    @ApiModelProperty(value = "用户邮箱",example = "1234567890@qq.com")
    @Email
    protected String email;

    @ApiModelProperty(value = "反馈意见", example = "正文正文正文")
    @Length(max = 200)
    protected String content;
}
