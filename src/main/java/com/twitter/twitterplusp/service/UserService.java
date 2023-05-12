package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.FansTop;
import com.twitter.twitterplusp.entity.User;

import java.util.List;
import java.util.Map;


public interface UserService extends IService<User> {

    /**
     * 判断用户是否已经登录，是返回true，并返回用户信息； 否返回false
     * @return
     */
    Map isLogin();

    /**
     * 用户注册
     * @param user
     * @return
     */
    R regist(User user);


    /**
     * 获取某个用户信息
     * @param userId
     * @return
     */
    Map getSomeoneUserInfo(Long userId);


    /**
     * 更新用户信息
     * @param user
     * @return
     */
    String updateSomeoneUserInfo(User user);


    List<User> getAllUserInfo();

    Boolean blockUser(List<Long> ids);

    Boolean unblockUser(List<Long> ids);

    FansTop getFansTop();
}
