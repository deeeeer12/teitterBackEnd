package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.User;

public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     * @param tweetId
     */
    void addComment(Long tweetId, User user,Comment comment);

}
