package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppUserMapper extends BaseMapper<User> {
}
