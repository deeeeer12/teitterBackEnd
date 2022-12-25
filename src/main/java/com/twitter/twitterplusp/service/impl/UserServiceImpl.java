package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.entity.UserRole;
import com.twitter.twitterplusp.mapper.UserMapper;
import com.twitter.twitterplusp.service.UserRoleService;
import com.twitter.twitterplusp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Map<String, Object> isLogin() {

        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authenticationToken =
                null;
        try {
            authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> map = new HashMap<>();
            map.put("status", "200");
            map.put("msg", "用户未登录");
            map.put("isLogin", false);
            map.put("userInfo", null);
            return map;
        }

        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        Map<String,Object> map = new HashMap<>();
        map.put("status","201");
        map.put("msg","用户登录成功");
        map.put("isLogin",true);
        map.put("userInfo",loginUser.getUser());
        return map;
    }

    @Override
    public R regist(User user) {
        String userName = user.getUserName();
        String userPassword = user.getUserPassword();
        //将用户的密码进行特殊加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPwd = passwordEncoder.encode(userPassword);
        user.setUserPassword(newPwd);

        //创建用户给一个默认头像
        user.setAvatarUrl("/images/avatarFile/default.jpg");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User usr = userService.getOne(queryWrapper);
        if(usr!=null){
            return R.error("当前用户名已存在");
        }

        //将新创建的用户存入用户表
        userService.save(user);
        Long uid = user.getUid();

        //再给新用户赋予角色，默认为普通用户 2
        UserRole userRole = new UserRole(uid,2);
        userRoleService.save(userRole);
        return R.success(null,"创建用户成功");
    }

}
