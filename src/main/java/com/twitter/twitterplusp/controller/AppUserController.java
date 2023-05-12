package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.service.AppUserService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.rmi.UnexpectedException;


@RestController
@RequestMapping("teitter/app/api/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    /**
     * 用户使用微信进行登录
     * @param js_code
     * @return
     * @throws Exception
     */
    @GetMapping("/login/{code}")
    public R loginWeChat(@PathVariable("code") String js_code) throws Exception {
        R result = appUserService.loginWeChat(js_code);

        return result;
    }

    /**
     * 进行用户与微信绑定操作
     * @param code
     * @return
     */
    @PostMapping("/binding")
    public R binding(String code) throws UnexpectedException {

        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        Long uid = loginUser.getUser().getUid();

        R result = appUserService.bindingUser(uid,code);

        return result;
    }
}