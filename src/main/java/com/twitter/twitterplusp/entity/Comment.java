package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_comment")
public class Comment {

    private Long id;

    private Long ParentTweetId;//父推文id

    private Long uid;

    private String commentContent;

    @TableField(fill = FieldFill.INSERT)
    private Long createDate;//评论时间

    //逻辑删除字段
    @TableLogic
    private Integer isDeleted;

}
