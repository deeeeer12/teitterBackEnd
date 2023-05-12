package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.TopicAndTweetMapper;
import com.twitter.twitterplusp.mapper.TopicMapper;
import com.twitter.twitterplusp.service.LikeService;
import com.twitter.twitterplusp.service.TopicService;
import com.twitter.twitterplusp.service.TweetService;
import com.twitter.twitterplusp.service.UserService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Autowired
    TopicMapper topicMapper;

    @Autowired
    TopicAndTweetMapper topicAndTweetMapper;


    @Autowired
    UserService userService;

    @Autowired
    TweetService tweetService;

    @Autowired
    LikeService likeService;
    @Override
    public String postTopic(Long uid, String topicName) {
        Topic topic = new Topic();
        topic.setTopicName(topicName);
        topic.setUid(uid);
        int insert = topicMapper.insert(topic);
        if (insert!=1){
            return "发布失败";
        }
        return "发布成功";
    }

    @Override
    public String delTopic(Long id) {

        int i = topicMapper.deleteById(id);
        if (i!=1){
            return "删除失败";
        }
        return "删除成功";
    }

    @Override
    public List<TweetDto> getTweetsByTopicId(Long topicId) {

        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        Long uid = loginUser.getUser().getUid();

        LambdaQueryWrapper<TopicAndTweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TopicAndTweet::getTopicId,topicId);

        //该集合中存放所有与传过来的topicId话题的推文id
        List<TopicAndTweet> topicAndTweets = topicAndTweetMapper.selectList(queryWrapper);

        //循环遍历，查询出推文内容
        Page<Tweet> pageInfo = new Page<>();
        Page<TweetDto> pageDtoInfo = new Page<>();

        for (TopicAndTweet topicAndTweet : topicAndTweets) {
            Long tweetId = topicAndTweet.getTweetId();
            LambdaQueryWrapper<Tweet> queryWrapperTweet = new LambdaQueryWrapper<>();
            queryWrapperTweet.eq(Tweet::getTweetId,tweetId)
                    .orderByDesc(Tweet::getLikeCount);
            tweetService.page(pageInfo, queryWrapperTweet);
        }

        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");

        //封装更详细的推文内容
        List<TweetDto> list = new ArrayList<>();
        List<Tweet> records = pageInfo.getRecords();
        for (Tweet item : records) {
            TweetDto tweetDto = new TweetDto();
            Long userId = item.getUid();
            Long tweetId = item.getTweetId();
            BeanUtils.copyProperties(item, tweetDto);
            //查找指定用户的信息
            User user = userService.getById(userId);
            String nickName = user.getNickName();
            String avatar_url = user.getAvatarUrl();
            String userName = user.getUserName();

            //查找推文的评论信息
            LambdaQueryWrapper<Tweet> queryWrapperComment = new LambdaQueryWrapper<>();
            queryWrapperComment.eq(Tweet::getParentTweetId, tweetId);
            List<Tweet> comments = tweetService.getBaseMapper().selectList(queryWrapperComment);

            //提取我们想要的评论信息
            List<Tweet> newComments = new ArrayList<>();
            for (Tweet obj : comments) {
                Tweet comment = new Tweet();
                BeanUtils.copyProperties(obj, comment, "id", "tweetId", "isDeleted");
                newComments.add(comment);
            }

            //查找推文的点赞信息
            LambdaQueryWrapper<Like> queryWrapperLike = new LambdaQueryWrapper<>();
            queryWrapperLike.eq(Like::getUid, uid)
                    .eq(Like::getTweetId, tweetId);
            Like like = likeService.getOne(queryWrapperLike);

            //封装当前登录用户对应的推文的点赞信息
            if (Objects.isNull(like) || like.getStatus()==0) {
                tweetDto.setLikeStatus(false);
            } else if (like.getStatus() == 1) {
                tweetDto.setLikeStatus(true);
            }

            //封装更多其他信息
            tweetDto.setAvatarUrl(avatar_url);
            tweetDto.setNickName(nickName);
            tweetDto.setUserName(userName);
            list.add(tweetDto);
        }

        return list;
    }
}
