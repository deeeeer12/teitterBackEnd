package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.MessageInfo;
import com.twitter.twitterplusp.mapper.MessageInfoMapper;
import com.twitter.twitterplusp.service.MessageInfoService;
import org.springframework.stereotype.Service;

@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements MessageInfoService {
}
