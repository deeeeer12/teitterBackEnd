package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
}
