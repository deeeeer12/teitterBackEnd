package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;

import java.util.List;

public interface TweetService extends IService<Tweet> {

    R send(Tweet tweet, String topicName, LoginUser loginUser, Long parentTweetId);

    /**
     * 获取全部忒文
     * @param pageNum
     * @return
     */
    R selectAllTwt(Integer pageNum,String keyWord);


    List getUserTweet(Long userId);

    Boolean delTweets(List<Long> ids);

    String delTweet(Long tweetId);


}
