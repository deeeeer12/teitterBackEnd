package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.FansTop;
import com.twitter.twitterplusp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teitter/api/chart")
public class ChartController {

    @Autowired
    private UserService userService;

    @PostMapping("/getFansTop")
    public R getFansTop(){

        FansTop result = userService.getFansTop();

        return R.success(result,"获取成功");

    }

}
