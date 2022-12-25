package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;

import java.util.List;
import java.util.Map;

public interface TweetService extends IService<Tweet> {

    R send(Tweet tweet, LoginUser loginUser);

    /**
     * 获取全部忒文
     * @param pageNum
     * @return
     */
    R selectAllTwt(Integer pageNum);


    List getUserTweet(Long userId);
}
