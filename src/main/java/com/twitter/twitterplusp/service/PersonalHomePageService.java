package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;

public interface PersonalHomePageService extends IService<Tweet> {
    R getUserTweets(User user);
}
