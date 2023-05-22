package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Fans;
import com.twitter.twitterplusp.entity.Follow;
import com.twitter.twitterplusp.service.FansService;
import com.twitter.twitterplusp.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeSet;

@Slf4j
@RestController
@RequestMapping("/teitter/v2/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private FansService fansService;

    /**
     * 关注or取关接口
     * @param userId
     * @param isFollow
     * @return
     */
    @PostMapping("/follow")
    public R follow(Long userId,Integer isFollow){

        String result = followService.follow(userId, isFollow);
        return R.success(null,result);

    }

    /**
     * 获取当前用户的关注列表
     * @return
     */
    @GetMapping("/getAllFollow")
    public R getAllFollow(Long uid){
        //获取当前登录用户的uid
//        Long uid = GetLoginUserInfo.getLoginUser().getUser().getUid();
        TreeSet<Follow> allFollows = followService.getAllFollow(uid);

        return R.success(allFollows,"获取关注列表成功");

    }

    /**
     * 获取当前用户的粉丝列表
     * @param uid
     * @return
     */
    @GetMapping("/getAllFans")
    public R getAllFans(Long uid){

        TreeSet<Fans> allFans = fansService.getAllFans(uid);

        return R.success(allFans,"获取粉丝列表成功");
    }

}
