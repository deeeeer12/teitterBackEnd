package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.Topic;

import java.util.List;

public interface TopicService extends IService<Topic> {
    String postTopic(Long uid, String topicName);

    String delTopic(Long id);

    List<TweetDto> getTweetsByTopicId(Long topicId);
}
