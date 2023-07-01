package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.UserMapper;
import com.twitter.twitterplusp.service.TweetService;
import com.twitter.twitterplusp.service.UserRoleService;
import com.twitter.twitterplusp.service.UserService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import com.twitter.twitterplusp.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private FollowServiceImpl followServiceImpl;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    public static final String BASE_URL = "https://www.heron.love:8888/";


    /**
     * 判断用户是否登录
     * @return
     */
    @Override
    public Map<String, Object> isLogin() {

        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authenticationToken = null;
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

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public R regist(User user) {
        String userName = user.getUserName();
        String userPassword = user.getUserPassword();
        //将用户的密码进行特殊加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPwd = passwordEncoder.encode(userPassword);
        user.setUserPassword(newPwd);

        //创建用户给一个默认头像
        user.setAvatarUrl(BASE_URL+"teitterfile/images/avatar_default.jpg");

        //创建用户给一个默认背景图片
        user.setBackgroundUrl(BASE_URL+"teitterfile/images/background_default.jpg");

        //创建用户给一个默认的个性签名
        user.setProfile("这个人很懒，什么都没有留下");

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

    /**
     * 返回某一个用户的信息
     * @param userId
     * @return
     */
    @Override
    public Map getSomeoneUserInfo(Long userId) {

        Long loginUid = GetLoginUserInfo.getLoginUser().getUser().getUid();

        //查询用户基本信息
        User userInfo = userService.getById(userId);

        //返回该用户和当前登录用户的两两关系
        List<Integer> integers = followServiceImpl.ptoPRelation(loginUid, userId);

        //封装用户两两关系
        userInfo.setPtoPRelation(integers);

        //封装用户发送的推文数量
        //根据uid查询用户发了多少条推文
        LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tweet::getUid,userId);
        int count = tweetService.count(queryWrapper);
        userInfo.setTweetCount(count);
        Map<String,Object> map = new HashMap<>();
        map.put("userInfo",userInfo);
        map.put("status",200);
        map.put("msg","获取用户信息成功");

        return map;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public String updateSomeoneUserInfo(User user) {
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        Long uid = loginUser.getUser().getUid();
        user.setUid(uid);

        if(user!=null){
            userService.updateById(user);
        }

        return "修改成功";
    }

    @Override
    public List<User> getAllUserInfo() {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStatus,1)
                .or()
                .eq(User::getStatus,0);


        List<User> users = userService.getBaseMapper().selectList(queryWrapper);

        return users;
    }

    @Override
    public Boolean blockUser(List<Long> ids) {

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.in(User::getUid,ids);

        updateWrapper.set(User::getStatus,0);

        userService.update(updateWrapper);

        return true;
    }

    @Override
    public Boolean unblockUser(List<Long> ids) {

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(User::getUid,ids);
        updateWrapper.set(User::getStatus,1);

        userService.update(updateWrapper);
        return true;
    }

    @Override
    public FansTop getFansTop() {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIsDeleted,0)
                        .orderByDesc(User::getFansCount)
                                .last("limit 5");
        List<User> users = userMapper.selectList(queryWrapper);

        FansTop fansTop = new FansTop();

        //用来存放用户昵称
        List<String> nickNames = new ArrayList<>();

        //用来存放用户的粉丝数量
        List<Long> fansCounts = new ArrayList<>();

        //用来存放用户的头像地址
        List<String> userAvatars = new ArrayList<>();

        for (User user : users) {
            String nickName = user.getNickName();
            nickNames.add(nickName);
            Long fansCount = user.getFansCount();
            fansCounts.add(fansCount);
            String avatarUrl = user.getAvatarUrl();
            userAvatars.add(avatarUrl);
        }

        //对数据进行封装返回
        fansTop.setNickName(nickNames);
        fansTop.setFansCount(fansCounts);
        fansTop.setUserAvatarUrl(userAvatars);

        return fansTop;
    }

}
