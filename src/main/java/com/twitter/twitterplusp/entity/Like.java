package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户点赞记录表
 */
@Data
@TableName("t_like")
public class Like {

    @TableId("id")
    private Long likeId;

    private Long tweetId;//被点赞的推文id

    private Long uid;//用户id

    private Integer status; //1代表点赞了 0代表取消赞

}
