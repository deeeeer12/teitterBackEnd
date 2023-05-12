package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_topic")
public class Topic {

    private Long id;

    private String topicName;//话题名称

    private Integer viewCount;//话题浏览量

    private Long uid;//发布人id

    @TableField(fill  = FieldFill.INSERT)//插入时填充字段
    private Long createDate;


}
