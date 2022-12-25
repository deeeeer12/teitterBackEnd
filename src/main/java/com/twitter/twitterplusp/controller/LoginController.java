package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teitter/api/user")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody User user){
        R result = loginService.login(user);
        return result;
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    public R logout(){

        R result = loginService.logout();

        return result;
    }

}
