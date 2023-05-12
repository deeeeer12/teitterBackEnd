package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.Message;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.MessageMapper;
import com.twitter.twitterplusp.model.MessageAndInfoModel;
import com.twitter.twitterplusp.service.MessageService;
import com.twitter.twitterplusp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{

    @Autowired
    private UserService userService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageService messageService;

    @Override
    public List<MessageAndInfoModel> getAllNotice(Long uid) {
        List<MessageAndInfoModel> total = messageMapper.selectNotice(uid);
        for (MessageAndInfoModel messageAndInfoModel : total) {
            Long senderId = messageAndInfoModel.getSenderId();
            User user = userService.getById(senderId);
            messageAndInfoModel.setNickName(user.getNickName());
            messageAndInfoModel.setAvatar_url(user.getAvatarUrl());
        }
        return total;
    }

    @Override
    public void updateStatus(Long msgId) {
        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Message::getStatus,1)
                        .eq(Message::getMessageId,msgId);
        messageMapper.update(null,updateWrapper);
    }

    @Override
    public void editStatusAll(Long uid) {

        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Message::getReceiverId,uid)
                        .setSql("STATUS = 1");
        messageService.update(updateWrapper);
    }

}
