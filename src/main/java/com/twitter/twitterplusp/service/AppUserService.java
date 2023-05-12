package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;

import java.rmi.UnexpectedException;

public interface AppUserService extends IService<User> {

    R loginWeChat(String code) throws UnexpectedException;

    R bindingUser(Long uid,String code) throws UnexpectedException;
}
