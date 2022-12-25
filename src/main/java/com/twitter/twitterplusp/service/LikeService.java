package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.entity.Like;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;

public interface LikeService extends IService<Like> {

    /**
     * 推文点赞
     * @param user
     * @param tweet
     */
    String incrlikeWithTweet(User user, Tweet tweet);

    /**
     * 推文取消赞
     * @param user
     * @param tweet
     */
    String decrlikeWithTweet(User user,Tweet tweet);
}
