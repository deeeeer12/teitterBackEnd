package com.twitter.twitterplusp.common;

import com.twitter.twitterplusp.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ResponseMessage {
    public int status;//请求状态，200成功，400失败
//    public int number;//在线人数
    public String message;//请求：getChats、sendMessage、loadMessage
    public String content;//消息内容
    public String sendUsernickname;//发送者
    public String receiveUsernickname;//接收者
    public List<User> users;//所有聊天
    public List<Message> messages;//所有的消息记录
}
