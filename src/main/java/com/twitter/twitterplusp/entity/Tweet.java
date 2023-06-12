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

    private Integer commentCount;

    private Integer pv;//浏览量

    private String tweetImg;

    private String tweetVideo;

    //逻辑删除字段
    @TableLogic
    private Integer isDeleted;

}
