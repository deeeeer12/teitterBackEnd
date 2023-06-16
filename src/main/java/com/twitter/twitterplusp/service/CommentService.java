package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;

import java.util.Map;

public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     * @param tweetId
     */
    void addComment(Long tweetId, User user, String content);

    Map getCommentByTweetId(Long tweetId);

    Map getCommentV2ByTweetId(Long tweetId);
}
