package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.TopicAndTweet;
import com.twitter.twitterplusp.mapper.TopicAndTweetMapper;
import com.twitter.twitterplusp.service.TopicAndTweetService;
import org.springframework.stereotype.Service;

@Service
public class TopicAndTweetServiceImpl extends ServiceImpl<TopicAndTweetMapper, TopicAndTweet> implements TopicAndTweetService {
}
