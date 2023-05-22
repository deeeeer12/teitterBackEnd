package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.TweetService;
import com.twitter.twitterplusp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teitter/v2/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    /**
     * 获取所有用户的信息
     * @return
     */
    @GetMapping("/getAllUserInfo")
    public R getAllUserInfo(){

        List<User> allUserInfo = userService.getAllUserInfo();
        return R.success(allUserInfo,"获取成功");

    }


    /**
     * 禁用用户
     * @param ids
     * @return
     */
    @PostMapping("/blockUser")
    public R blockUser(@RequestParam List<Long> ids){

        Boolean result = userService.blockUser(ids);

        return R.success(result,"成功");

    }

    /**
     * 解封用户
     * @param ids
     * @return
     */
    @PostMapping("/unblockUser")
    public R unblockUser(@RequestParam List<Long> ids){
        Boolean result = userService.unblockUser(ids);

        return R.success(result,"成功");
    }

    /**
     * 根据推文id删除推文
     * @param ids
     * @return
     */
    @PostMapping("/delTweets")
    public R delTweets(@RequestParam List<Long> ids){

        Boolean result = tweetService.delTweets(ids);

        if (result == false){
            return R.error("删除失败");
        }
        return R.success(null,"删除成功");

    }

}
