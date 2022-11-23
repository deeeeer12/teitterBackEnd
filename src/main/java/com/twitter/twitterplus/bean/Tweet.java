package com.twitter.twitterplus.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@TableName("t_tweet")
public class Tweet {

    @TableId("tweet_id")
    private Long tweetId;
    private String nickName;
    private String userName;
    private String content;
    private int likeCount;
    private int commentCount;
    private int forwardCount;
    private Long updatetime;
    @TableLogic
    private int isDeleted;

}
