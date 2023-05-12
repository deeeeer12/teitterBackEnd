package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.AppUserMapper;
import com.twitter.twitterplusp.service.AppUserService;
import com.twitter.twitterplusp.service.UserService;
import com.twitter.twitterplusp.utils.WeChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;

@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, User> implements AppUserService {


    @Autowired
    private UserService userService;

    @Override
    public R loginWeChat(String code) throws UnexpectedException {

        String openid = WeChatUtils.getOpenid(code);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenId, openid);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return R.error("用户尚未绑定微信");
        }

        return R.success(user, "微信登录成功");

    }

    @Override
    public R bindingUser(Long uid, String code) throws UnexpectedException {

        String openid = WeChatUtils.getOpenid(code);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUid, uid)
                .set(User::getOpenId, openid);
        boolean result = userService.update(updateWrapper);
        if (result != true) {
            return R.error("绑定出现错误");
        }

        return R.success(null, "用户绑定成功");
    }
}
