package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.CommentService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/teitter/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("/addComment")
    public R<String> addComment(Comment comment,Long tweetId){
        //从SecurityContextHolder中取出用户信息
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User userInfo = loginUser.getUser();

        if (userInfo==null){
            return R.error("登陆后解锁评论功能~");
        }

        commentService.addComment(tweetId,userInfo,comment);

        return R.success(null,"评论成功");

    }

    @GetMapping("/getComment/{tweetId}")
    public Map getComment(@PathVariable("tweetId") Long tweetId){

        Map result = commentService.getCommentByTweetId(tweetId);

        return result;
    }

}
