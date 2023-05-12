package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import org.springframework.http.server.ServerHttpRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
    R logout(HttpServletRequest request,HttpServletResponse response);

}
