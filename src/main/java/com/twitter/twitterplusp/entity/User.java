package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_user")
public class User{

    @TableId("uid")
    private Long uid;

    private String userName;

    private String userPassword;

    private String nickName;

    @TableField(fill  = FieldFill.INSERT)//插入时填充字段
    private Long createDate;

    private String avatarUrl;

    private Integer status;

    private String profile;

    private String backgroundUrl;

    private String openId;//微信登录唯一凭证

    private Long fansCount;//粉丝数量

    private Long followsCount;//关注数量

    @TableField(exist = false)
    private List<Integer> ptoPRelation;//用户两两关系

    @TableField(exist = false)
    private Integer tweetCount;//推文数量

    //逻辑删除字段
    @TableLogic
    private Integer isDeleted;



}
