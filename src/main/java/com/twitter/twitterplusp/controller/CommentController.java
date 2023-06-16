package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.CommentService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/teitter/v2/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * 添加评论按钮
     * @param tweetId
     * @return
     */
    @PostMapping("/addComment")
    public R<String> addComment(String content, Long tweetId){
        //从SecurityContextHolder中取出用户信息
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User userInfo = loginUser.getUser();

        if (userInfo==null){
            return R.error("登陆后解锁评论功能~");
        }

        commentService.addComment(tweetId,userInfo,content);

        return R.success(null,"评论成功");

    }

    /**
     * 根据推文获取评论
     * @param tweetId
     * @return
     */
    @GetMapping("/getComment/{tweetId}")
    public Map getComment(@PathVariable("tweetId") Long tweetId){

        Map result = commentService. getCommentByTweetId(tweetId);

        return result;
    }

    @GetMapping("/getComment/v2/{tweetId}")
    public Map getCommentV2(@PathVariable("tweetId") Long tweetId){

        Map result = commentService.getCommentV2ByTweetId(tweetId);

        return null;
    }

}
