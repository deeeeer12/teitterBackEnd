package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/teitter/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     *
     * @return
     */
    @PostMapping("/regist")
    public R save(User user) {

        R result = userService.regist(user);

        return result;

    }


    /**
     * 判断用户是否已经登录，是返回true，并返回用户信息； 否返回false
     *
     * @return
     */
    @GetMapping("/isLogin")
    public Map isLogin() {

        Map result = userService.isLogin();

        return result;
    }

    /**
     * 获取某个用户的个人信息，可用于信息回显
     *
     * @param userId
     * @return
     */
    @GetMapping("/getUserInfo/{uid}")
    public Map getSomeoneUserInfo(@PathVariable("uid") Long userId) {
        Map result = userService.getSomeoneUserInfo(userId);
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/editUserInfo")
    public R updateSomeoneUserInfo(User user) {

        String result = userService.updateSomeoneUserInfo(user);

        return R.success(null, result);
    }

}
