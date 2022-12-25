package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.PersonalHomePageMapper;
import com.twitter.twitterplusp.service.PersonalHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PersonalHomePageServiceImpl extends ServiceImpl<PersonalHomePageMapper, Tweet> implements PersonalHomePageService {

    @Autowired
    private PersonalHomePageService personalHomePageService;

    @Override
    public R getUserTweets(User user) {
        Long userId = user.getUid();
        LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tweet::getUid,userId);
        List<Tweet> tweets = personalHomePageService.getBaseMapper().selectList(queryWrapper);
        if (Objects.isNull(tweets)){
            return R.error("该用户还没有忒文");
        }
        return R.success(tweets,"获取该用户忒文成功");
    }
}
