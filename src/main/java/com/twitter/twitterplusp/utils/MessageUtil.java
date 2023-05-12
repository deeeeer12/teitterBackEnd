package com.twitter.twitterplusp.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MessageUtil {

    /**
     * 将信息格式封装返回
     * @param senderId
     * @param receiverId
     * @param msgInfoId
     * @return
     */
    public MessageUtil getMessages(MessageUtil messageUtil){

        MessageUtil msg = new MessageUtil();

        return msg;

    }


    /**
     * 将信息内容封装返回
     * @param targetId
     * @param title
     * @param content
     * @param msgType
     * @return
     */
    public Map<String,Object> getMessageInfo(Long targetId,String title,String content,int msgType){

        Map<String,Object> map = new HashMap<>();
        map.put("targetId",targetId);
        map.put("title",title);
        map.put("content",content);
        map.put("msgType",msgType);

        return map;

    }

}
