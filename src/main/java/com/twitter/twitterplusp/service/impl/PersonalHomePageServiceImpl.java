package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.PersonalHomePageMapper;
import com.twitter.twitterplusp.service.PersonalHomePageService;
import com.twitter.twitterplusp.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

@Service
public class PersonalHomePageServiceImpl extends ServiceImpl<PersonalHomePageMapper, Tweet> implements PersonalHomePageService {

    @Autowired
    private PersonalHomePageService personalHomePageService;

    @Autowired
    private TweetService tweetService;

    @Override
    public R getUserTweets(User user) {
        Long userId = user.getUid();
        LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tweet::getUid,userId);
        List<Tweet> tweets = personalHomePageService.getBaseMapper().selectList(queryWrapper);

        for (Tweet tweet : tweets) {
            //若该推文的父ID不等于null，即它为子推文，则根据ParentTweetId查询parentTweet的作者nickName并封装返回
            Long parentTweetId = tweet.getParentTweetId();
            if (!ObjectUtils.isEmpty(parentTweetId)){
                LambdaQueryWrapper<Tweet> queryParentNickName = new LambdaQueryWrapper<>();
                queryParentNickName.eq(Tweet::getTweetId,parentTweetId);
                Tweet parentTweet = tweetService.getOne(queryParentNickName);
                //最终结果:nickName
                String nickName = parentTweet.getNickName();
                tweet.setRepliedTo(nickName);
            }
        }

        if (Objects.isNull(tweets)){
            return R.error("该用户还没有忒文");
        }
        return R.success(tweets,"获取该用户忒文成功");
    }
}
