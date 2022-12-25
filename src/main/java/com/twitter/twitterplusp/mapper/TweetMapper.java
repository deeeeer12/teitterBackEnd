package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.Tweet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TweetMapper extends BaseMapper<Tweet> {
}
