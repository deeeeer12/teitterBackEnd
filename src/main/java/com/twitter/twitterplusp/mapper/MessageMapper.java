package com.twitter.twitterplusp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.twitter.twitterplusp.entity.Message;
import com.twitter.twitterplusp.model.MessageAndInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    List<MessageAndInfoModel> selectNotice(@Param("receiverId") Long receiverId);

}
