package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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


    User getSomeoneUserInfo(Long userId);


    String updateSomeoneUserInfo(User user);

}
