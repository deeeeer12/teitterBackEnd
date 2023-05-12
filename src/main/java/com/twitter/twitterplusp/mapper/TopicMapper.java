package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}
