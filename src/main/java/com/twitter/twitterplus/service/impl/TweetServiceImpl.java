package com.twitter.twitterplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplus.bean.Tweet;
import com.twitter.twitterplus.mapper.TweetMapper;
import com.twitter.twitterplus.service.TweetService;

public class TweetServiceImpl extends ServiceImpl<TweetMapper, Tweet> implements TweetService {
}
