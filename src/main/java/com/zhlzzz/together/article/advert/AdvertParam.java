package com.zhlzzz.together.article.advert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

@ApiModel(description = "文章页轮播图参数")
@Data
@Builder
public class AdvertParam implements Serializable {

    @ApiModelProperty(value = "文章ID", example = "1")
    protected Long articleId;

    @ApiModelProperty(value = "广告标题", example = "吃鸡宝典")
    protected String title;

    @ApiModelProperty(value = "广告图片", example = "http://www.baidu.com")
    @URL
    protected String advertUrl;

}
