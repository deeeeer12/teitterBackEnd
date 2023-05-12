package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.CustomException;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.LikeMapper;
import com.twitter.twitterplusp.service.LikeService;
import com.twitter.twitterplusp.service.MessageInfoService;
import com.twitter.twitterplusp.service.MessageService;
import com.twitter.twitterplusp.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Slf4j
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {


    @Autowired
    private TweetService tweetService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageInfoService messageInfoService;

    /**
     * 点赞
     * @param user
     * @param tweet
     */
    @Override
    @Transactional
    public String incrlikeWithTweet(User user,Tweet tweet) {

        if(user != null){

            LambdaQueryWrapper<Tweet> queryWrapperisExist = new LambdaQueryWrapper<>();
            queryWrapperisExist.eq(Tweet::getTweetId,tweet.getTweetId());
            Tweet isExist = tweetService.getOne(queryWrapperisExist);
            if (Objects.isNull(isExist)){
                return "当前推文不存在";
            }

            //点赞前先去点赞表中查询用户和该条推文所对应的status，若为0.则可以点赞，若为1，则不可
            LambdaQueryWrapper<Like> queryWrapperStatus = new LambdaQueryWrapper<>();
            queryWrapperStatus.eq(Like::getUid,user.getUid())
                    .eq(Like::getTweetId,tweet.getTweetId());
            Like likeRecord = this.getOne(queryWrapperStatus);


            if(likeRecord == null || (likeRecord.getStatus()==0)){

                if(likeRecord!=null){
                    Like oldLike = new Like();
                    oldLike.setStatus(1);
                    oldLike.setLikeId(likeRecord.getLikeId());
                    this.updateById(oldLike);
                    //点赞成功后，推文点赞出+1
                    LambdaUpdateWrapper<Tweet> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Tweet::getTweetId,tweet.getTweetId())
                            .setSql("`like_count`=`like_count`+1");
                    tweetService.update(updateWrapper);
                    return  "点赞成功！";
                }

                //向点赞表中存储相关信息
                Like newLike = new Like();
                newLike.setTweetId(tweet.getTweetId());
                newLike.setUid(user.getUid());
                newLike.setStatus(1);
                this.save(newLike);

                //向消息详情表中存储相关信息
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setTitle(null);
                LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Tweet::getTweetId,tweet.getTweetId());
                Tweet tweetServiceOne = tweetService.getOne(queryWrapper);
                //只取文章内容的前十个字符

                String content = tweetServiceOne.getContent();
                if(content.length()>30){
                    content = content.substring(0,30);
                }
                messageInfo.setContent(content+"... ...");
                messageInfo.setMsgType(1);
                messageInfo.setTweetId(tweet.getTweetId());
                messageInfoService.save(messageInfo);
                Long msgInfoId = messageInfo.getMsgInfoId();

                //向消息通知表中存储相关信息
                Message message = new Message();
                message.setSenderId(user.getUid());
                LambdaQueryWrapper<Tweet> qw = new LambdaQueryWrapper<>();
                qw.eq(Tweet::getTweetId,tweet.getTweetId());
                Tweet twt = tweetService.getOne(qw);
                message.setReceiverId(twt.getUid());
                message.setMsgInfoId(msgInfoId);
                messageService.save(message);

                //点赞成功后，推文点赞数+1
                LambdaUpdateWrapper<Tweet> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Tweet::getTweetId,tweet.getTweetId())
                        .setSql("`like_count`=`like_count`+1");
                tweetService.update(updateWrapper);
                return "点赞成功";

            }
            return "你已经点过赞了，请不要重复操作";

        }
        return "登录解锁更多功能~";

    }

    /**
     * 取消赞
     * @param user
     * @param tweet
     */
    @Override
    @Transactional
    public String decrlikeWithTweet(User user, Tweet tweet) {
        LambdaQueryWrapper<Tweet> queryWrapperisExist = new LambdaQueryWrapper<>();
        queryWrapperisExist.eq(Tweet::getTweetId,tweet.getTweetId());
        Tweet isExist = tweetService.getOne(queryWrapperisExist);
        if (isExist==null){
            return "当前推文不存在";
        }

        LambdaQueryWrapper<Like> queryWrapperStatus = new LambdaQueryWrapper<>();
        queryWrapperStatus.eq(Like::getUid,user.getUid())
                .eq(Like::getTweetId,tweet.getTweetId());
        Like one = this.getOne(queryWrapperStatus);

        if(one.getStatus()==0){
            return "当前忒文已经取消赞了，请不要重复操作";
        }

        //取消赞成功后,根据用户id和推文id定位点赞记录,并且修改status为0
        Like updateLike = new Like();
        updateLike.setLikeId(one.getLikeId());
        updateLike.setStatus(0);
        this.updateById(updateLike);



        //根据tweetId查出对应的推文对象，以获取该推文的点赞数量
        LambdaQueryWrapper<Tweet> queryWrapperTweet = new LambdaQueryWrapper<>();
        queryWrapperTweet.eq(Tweet::getTweetId,tweet.getTweetId());
        Tweet oldTweet = tweetService.getOne(queryWrapperTweet);
        Integer likeCount = oldTweet.getLikeCount();
        tweet.setLikeCount(likeCount);

        if (tweet.getLikeCount()<=0){
            throw new CustomException("当前忒文点赞数已经为0");
        }

        //取消赞成功后，推文点赞数-1
        LambdaUpdateWrapper<Tweet> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Tweet::getTweetId,tweet.getTweetId())
                    .setSql("`like_count`=`like_count`-1");

        tweetService.update(updateWrapper);

        return "取消赞成功";

    }
}
