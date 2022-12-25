package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.TweetService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teitter/api/tweet")
public class TweetController {

    @Autowired
    private TweetService tweetService;

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private LikeService likeService;


    /**
     * 发送忒文
     * @param tweet
     * @return
     */
    @PostMapping("/sendTwt")
    public R send(Tweet tweet){
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        R result = tweetService.send(tweet, loginUser);
        return result;
    }

    /**
     * 分页展示全部推文
     * @param pageNum
     * @return
     */
    @GetMapping("/getAllTweet/{pageNum}")
    public R<Page> page(@PathVariable("pageNum") Integer pageNum){

        R result = tweetService.selectAllTwt(pageNum);

        return result;

    }

    /**
     * 获取指定用户的所有推文
     * @param userId
     * @return
     */
    @GetMapping("/getUserTweet/{uid}")
    public R getUserTweet(@PathVariable("uid") Long userId){

        List userTweet = tweetService.getUserTweet(userId);
        if (userTweet ==null){
            R result = new R(400,"获取用户推文失败",null,null);
            return result;
        }

        R result = new R(200,"获取用户推文成功",userTweet,null);

        return result;
    }

}
