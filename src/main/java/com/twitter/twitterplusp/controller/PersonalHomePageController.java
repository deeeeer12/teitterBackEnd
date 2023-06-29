package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.PersonalHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teitter/v2/api/tweet")
public class PersonalHomePageController {


    @Autowired
    private PersonalHomePageService personalHomePageService;

    /**
     * 获取指定用户的所有推文
     * @param user
     * @return
     */
    @GetMapping("/getUserTweets")
    public R getUserTweets(User user){
        R result = personalHomePageService.getUserTweets(user);

        return result;
    }

}
