package com.twitter.twitterplusp.utils;

import com.twitter.twitterplusp.entity.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取已登录的用户信息
 */
public class GetLoginUserInfo {


    public static LoginUser getLoginUser(){
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();

        return loginUser;

    }

}
