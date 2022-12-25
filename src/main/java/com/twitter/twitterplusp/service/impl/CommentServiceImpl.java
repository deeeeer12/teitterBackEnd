package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.dto.CommentDto;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.CommentMapper;
import com.twitter.twitterplusp.service.CommentService;
import com.twitter.twitterplusp.service.TweetService;
import com.twitter.twitterplusp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    /**
     * 添加评论
     * @param tweetId
     */
    @Override
    public void addComment(Long tweetId, User user,Comment comment) {

        comment.setUid(user.getUid());
        comment.setTweetId(tweetId);
        this.save(comment);

    }

    @Override
    public Map getCommentByTweetId(Long tweetId) {

        //根据推文id查询推文内容
        LambdaQueryWrapper<Tweet> queryWrapperTweet = new LambdaQueryWrapper<>();
        queryWrapperTweet.eq(Tweet::getTweetId,tweetId);
        Tweet tweet = tweetService.getOne(queryWrapperTweet);

        //根据tweetId查询出当前推文的所有评论以及对应的用户信息，封装到CommentDto中
        LambdaQueryWrapper<Comment> queryWrapperComment = new LambdaQueryWrapper<>();
        queryWrapperComment.eq(Comment::getTweetId,tweetId)
                .orderByDesc(Comment::getCreateDate);

        List<Comment> comments = commentService.getBaseMapper().selectList(queryWrapperComment);

        //提取我们想要的评论信息,并添加评论对应的用户信息
        List<Comment> newComments = new ArrayList<>();
        for (Comment obj:comments){
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(obj,commentDto,"id","tweetId","isDeleted");
            Long userId = obj.getUid();
            User userInfo = userService.getById(userId);
            BeanUtils.copyProperties(userInfo,commentDto);
            newComments.add(commentDto);
        }
        //查询出每一条评论的用户信息
        Map<String,Object> map = new HashMap<>();
        map.put("tweet",tweet);
        map.put("comments",newComments);
        map.put("msg","获取推文评论成功");
        map.put("status",200);

        return map;
    }
}
