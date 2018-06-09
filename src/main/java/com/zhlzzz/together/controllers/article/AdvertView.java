package com.zhlzzz.together.controllers.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zhlzzz.together.article.advert.Advert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(description = "广告")
@JsonPropertyOrder({"id","articleId","title","advertUrl","createTime"})
@RequiredArgsConstructor
public class AdvertView {

    @NonNull
    private final Advert advert;

    @ApiModelProperty(name = "广告ID",example = "1")
    public Long getId(){
        return advert.getId();
    }

    @ApiModelProperty(name ="文章id",example = "123")
    public Long getArticleId(){
        return advert.getArticleId();
    }

    @ApiModelProperty(name ="广告标题",example = "吃鸡宝典")
    public String getTitle() { return advert.getTitle(); }

    @ApiModelProperty(name="广告图片",example = "http://www.baidu.com")
    public String getAdvertUrl(){
        return this.advert.getAdvertUrl();
    }

    @ApiModelProperty(name = "创建时间",example = "2017-12-13 12:03:20")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0800")
    public LocalDateTime getCreateTime(){
        return this.advert.getCreateTime();
    }

}
