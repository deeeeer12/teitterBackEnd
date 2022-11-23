package com.twitter.twitterplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplus.bean.Tweet;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetMapper extends BaseMapper<Tweet> {
}
