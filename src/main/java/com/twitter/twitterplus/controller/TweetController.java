package com.twitter.twitterplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twitter.twitterplus.bean.Tweet;
import com.twitter.twitterplus.mapper.TweetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
public class TweetController {

    @Autowired
    TweetMapper tweetMapper;

    /**
     * 发送推文
     * @param tweet
     * @return
     */
    @PostMapping("/teitter/api/sendTwt")
    public Map sendTwt(Tweet tweet, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        if (tweet.getContent()!=null){
            //设置时间戳
            tweet.setUpdatetime(System.currentTimeMillis());
            tweet.setNickName((String) request.getSession().getAttribute("nickName"));
            tweet.setUserName("@"+request.getSession().getAttribute("userName"));
            tweetMapper.insert(tweet);
            map.put("message","发布推文成功！");
            map.put("status","200");
        }else {
            map.put("message","发布推文失败！");
            map.put("status","400");
        }

        return map;
    }

    /**
     * 获取全部推文
     * @return
     */
    @GetMapping("/teitter/api/getAllTweet/{pageNum}" )
    public Map getAllTweet(@PathVariable("pageNum") Integer pageNum,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //验证是否登录，如果已经登陆，返回该用户的用户信息
        if(request.getSession().getAttribute("userName")!=null){
            map.put("isLogin",true);
            map.put("userInfo",request.getSession().getAttribute("userInfo"));
        }else{
            map.put("isLogin",false);
        }
        Page<Tweet> page = new Page<>(pageNum,10);
        QueryWrapper<Tweet> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("updatetime");
        Page<Tweet> tweets = tweetMapper.selectPage(page, wrapper);
        if(pageNum>page.getPages()){
            map.put("message","已超过最大页码数");
        }else{
            map.put("message","获取首页推文成功");
            map.put("status","200");
            map.put("currentPageNum",pageNum);
            map.put("teitterCount",page.getTotal());
            map.put("totalPageNum",page.getPages());
            map.put("data", tweets.getRecords());
        }
        return map;
    }

}
