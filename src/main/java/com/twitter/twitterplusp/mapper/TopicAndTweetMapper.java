package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.TopicAndTweet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicAndTweetMapper extends BaseMapper<TopicAndTweet> {
}
