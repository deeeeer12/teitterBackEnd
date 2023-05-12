package com.twitter.twitterplusp.model;

import lombok.Data;

@Data
public class MessageAndInfoModel {

    private Long senderId;//发送通知的人的ID，用来查询其昵称及头像地址

    private String nickName;//发出点赞的用户的昵称

    private String avatar_url; // 点赞用户的头像地址

    private Long messageId; //通知ID 用于修改status

    private String content;//推文大致内容

    private Long tweetId; //被点赞/评论的推文ID

    private String comment;//评论内容，点赞时为null

    private String createDate; //通知日期

    private Boolean status; //查看状态 1已查看，0未查看

    private Integer msgType;//通知类型

}
