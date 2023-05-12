package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class MessageInfo {

    @TableId("msg_info_id")
    private Long msgInfoId;

    private Long tweetId; // 推文ID

    private String title; //推文标题

    private String content; //推文大致内容

    private String comment; //评论内容

    private int msgType; //消息类型 1点赞，评论，3关注，回复评论，5系统通知

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long createDate;

}
