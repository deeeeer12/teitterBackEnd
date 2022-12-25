package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/teitter/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 注册用户
     * @return
     */
    @PostMapping("/regist")
    public R save( User user){

        R result = userService.regist(user);

        return result;

    }


    /**
     * 判断用户是否已经登录，是返回true，并返回用户信息； 否返回false
     * @return
     */
    @GetMapping("/isLogin")
    public Map isLogin(){

        Map result = userService.isLogin();

        return result;
    }

}
