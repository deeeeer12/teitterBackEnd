package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.Fans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FansMapper extends BaseMapper<Fans> {
    List<Integer> getPpRelation(@Param("loginUid") Long loginUid, @Param("otherUid") Long otherUid);
}
