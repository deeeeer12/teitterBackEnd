package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.entity.Message;
import com.twitter.twitterplusp.model.MessageAndInfoModel;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<MessageAndInfoModel> getAllNotice(Long uid);


    void updateStatus(Long msgId);

    void editStatusAll(Long uid);
}
