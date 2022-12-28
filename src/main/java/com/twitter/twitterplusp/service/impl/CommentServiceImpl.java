package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.dto.CommentDto;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.CommentMapper;
import com.twitter.twitterplusp.service.CommentService;
import com.twitter.twitterplusp.service.LikeService;
import com.twitter.twitterplusp.service.TweetService;
import com.twitter.twitterplusp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private LikeService likeService;

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

    /**
     * 获取推文所有评论
     * @param tweetId
     * @return
     */
    @Override
    public Map getCommentByTweetId(Long tweetId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        //根据推文id查询推文内容
        LambdaQueryWrapper<Tweet> queryWrapperTweet = new LambdaQueryWrapper<>();
        queryWrapperTweet.eq(Tweet::getTweetId,tweetId);
        Tweet tweet = tweetService.getOne(queryWrapperTweet);

        TweetDto tweetDto = new TweetDto();

        BeanUtils.copyProperties(tweet,tweetDto);

        if (!"anonymousUser".equals(name)){
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            //查找推文的点赞信息
            LambdaQueryWrapper<Like> queryWrapperLike = new LambdaQueryWrapper<>();
            queryWrapperLike.eq(Like::getUid,loginUser.getUser().getUid())
                    .eq(Like::getTweetId,tweetId);
            Like like = likeService.getOne(queryWrapperLike);

            //封装当前登录用户对应的推文的点赞信息
            if (Objects.isNull(like)){
                tweetDto.setLikeStatus(false);
            }else if(like.getStatus()==1) {
                tweetDto.setLikeStatus(true);
            }
        }else {
            tweetDto.setLikeStatus(false);
        }

        //获取推文的用户id
        Long uid = tweet.getUid();

        //根据推文id查询用户信息
        LambdaQueryWrapper<User> queryWrapperUser = new LambdaQueryWrapper<>();
        queryWrapperUser.eq(User::getUid,uid);
        User user = userService.getOne(queryWrapperUser);

        //封装更多用户信息
        //将用户信息拷贝到tweetDto中一并返回
        BeanUtils.copyProperties(user,tweetDto);
        BeanUtils.copyProperties(tweet,tweetDto);


        //根据tweetId查询出当前推文的所有评论，封装到CommentDto中·
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
            BeanUtils.copyProperties(userInfo,commentDto,"createDate");
            newComments.add(commentDto);
        }
        //查询出每一条评论的用户信息
        Map<String,Object> map = new HashMap<>();
        map.put("tweet",tweetDto);
        map.put("comments",newComments);
        map.put("msg","获取推文评论成功");
        map.put("status",200);

        return map;
    }
}
