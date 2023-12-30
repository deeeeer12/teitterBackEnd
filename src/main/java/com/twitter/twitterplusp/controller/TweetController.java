package com.twitter.twitterplusp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.service.TweetService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/teitter/v2/api/tweet")
public class TweetController {

    @Autowired
    private TweetService tweetService;


    /**
     * 发送忒文
     * @param tweet
     * @return
     */
    @PostMapping("/sendTwt")
    public R send(Tweet tweet,String topicName,Long parentTweetId){
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        R result = tweetService.send(tweet,topicName,loginUser,parentTweetId);
        return result;
    }

    /**
     * 分页展示推文
     * @param pageNum
     * @return
     */
    @PostMapping("/getAllTweet")
    public R<Page> page(Integer pageNum, String keyWord){

        R result = tweetService.selectAllTwt(pageNum,keyWord);

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

    /**
     * 根据tweetId删除推文
     * @param tweetId
     * @return
     */
    @PostMapping("/delTweet")
    public R delTweetPersonal(Long tweetId){

        //删除推文
        String result = tweetService.delTweet(tweetId);

        return R.success(null,result);
    }

}
