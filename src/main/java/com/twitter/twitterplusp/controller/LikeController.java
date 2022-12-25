package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.LikeService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/teitter/api/tweet")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 推文点赞
     * @param tweet
     * @return
     */
    @PostMapping("/like")
    public R<String> incrlikeWithTweet( Tweet tweet){
        //从SecurityContextHolder中取出用户信息
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User userInfo = loginUser.getUser();
        if(userInfo == null){
           return R.error("登录解锁更多功能~");
        }
        String msg = likeService.incrlikeWithTweet(userInfo, tweet);

        return R.success(null,msg);
    }

    /**
     * 推文取消赞
     * @param tweet
     * @return
     */
    @PostMapping("/unLike")
    public R<String> decrlikeWithTweet(Tweet tweet){
        //从SecurityContextHolder中取出用户信息
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User userInfo = loginUser.getUser();
        if (userInfo == null){
            return R.error("登录解锁更多功能~");
        }
        String msg = likeService.decrlikeWithTweet(userInfo, tweet);

        return R.success(null,msg);
    }

}
