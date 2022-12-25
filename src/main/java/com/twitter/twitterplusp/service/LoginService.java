package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;


public interface LoginService extends IService<User> {
    /**
     * 用户登录
     * @param user
     * @return
     */
    R login(User user);

    /**
     * 注销当前登录的用户
     * @return
     */
    R logout();

}
