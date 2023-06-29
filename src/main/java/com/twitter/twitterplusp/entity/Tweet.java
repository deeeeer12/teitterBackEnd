package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("t_tweet")
public class Tweet {

    @TableId("tweet_id")
    private Long tweetId;

    private Long uid;

    private Long parentTweetId;//推文的父ID

    private Integer level;//推文级别0/1

    private String content;

    private String nickName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long createDate;

    private Integer likeCount;

    @TableField(exist = false)//与数据库无关
    private String repliedNickNameTo;//若推文为回复推文，则有被回复者nickName

    @TableField(exist = false)//与数据库无关
    private String repliedUserNameTo;//若推文为回复推文，则有被回复者userName

    private Integer commentCount;

    private Integer pv;//浏览量

    private String tweetImg;

    private String tweetVideo;

    //逻辑删除字段
    @TableLogic
    private Integer isDeleted;

}
