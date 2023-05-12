package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.model.MessageAndInfoModel;
import com.twitter.twitterplusp.service.MessageService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teitter/api/notice")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取当前登录用户的所有通知
     * @return
     */
    @GetMapping("/getAllNotice")
    public R getAllNotice(){
        //从SecurityContextHolder中取出用户信息
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User userInfo = loginUser.getUser();
        Long uid = userInfo.getUid();
        List<MessageAndInfoModel> total = messageService.getAllNotice(uid);
        return R.success(total,"获取全部通知成功");
    }

    /**
     * 修改信息的已读状态
     * @param msgId
     * @return
     */
    @PostMapping("/editStatus")
    public R editStatus(Long msgId){
        messageService.updateStatus(msgId);

        return R.success(null,"信息状态修改成功");
    }

    /**
     * 一键已读
     * @return
     */
    @PostMapping("/editStatusAll")
    public R editStatusAll(){
        //从SecurityContextHolder中取出用户信息
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User userInfo = loginUser.getUser();
        Long uid = userInfo.getUid();
        messageService.editStatusAll(uid);

        return R.success(null,"操作成功");

    }

}
