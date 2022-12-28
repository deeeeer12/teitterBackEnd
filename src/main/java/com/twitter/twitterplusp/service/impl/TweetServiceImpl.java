package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.TweetMapper;
import com.twitter.twitterplusp.service.CommentService;
import com.twitter.twitterplusp.service.LikeService;
import com.twitter.twitterplusp.service.TweetService;
import com.twitter.twitterplusp.service.UserService;
import com.twitter.twitterplusp.utils.TweetFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TweetServiceImpl extends ServiceImpl<TweetMapper, Tweet> implements TweetService {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    public static final String MY_URL = "https://www.heron.love:8888";

    public R send(Tweet tweet, LoginUser loginUser, MultipartFile file) {

        if(loginUser == null){
            return R.error("请先登录再发忒");
        }

        if(tweet.getContent()==""){
            return R.error("内容不能为空");
        }

//        /images/tweetFile/admin14123123123123.jpg
        tweet.setUid(loginUser.getUser().getUid());
        try {
            String imgUrl = TweetFileUtil.uplods(file,loginUser.getUsername()+System.currentTimeMillis());
            String sqlImgUrl = MY_URL+imgUrl;
            tweet.setTweetImg(sqlImgUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tweetService.save(tweet);
        return R.success(null,"发忒成功");
    }


    /**
     * 获取忒文首页
     * @param pageNum
     * @return
     */
    @Override
    public R selectAllTwt(Integer pageNum) {

//        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (!"anonymousUser".equals(name)){
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            Page<Tweet> pageInfo = new Page<>(pageNum,20);
            Page<TweetDto> pageDto = new Page<>();

            LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
            //根据发忒时间降序排序
            queryWrapper.orderByDesc(Tweet::getCreateDate);
            tweetService.page(pageInfo,queryWrapper);

            BeanUtils.copyProperties(pageInfo,pageDto,"records");

            //用来封装推文信息和部分用户信息
            List<TweetDto> list = new ArrayList<>();

            List<Tweet> records = pageInfo.getRecords();
            for(Tweet item:records){
                TweetDto tweetDto = new TweetDto();
                Long uid = item.getUid();
                Long tweetId = item.getTweetId();
                BeanUtils.copyProperties(item,tweetDto);
                //查找指定用户的信息
                User user = userService.getById(uid);
                String nickName = user.getNickName();
                String avatar_url = user.getAvatarUrl();
                String userName = user.getUserName();

                //查找推文的评论信息
                LambdaQueryWrapper<Comment> queryWrapperComment = new LambdaQueryWrapper<>();
                queryWrapperComment.eq(Comment::getTweetId,tweetId);
                List<Comment> comments = commentService.getBaseMapper().selectList(queryWrapperComment);

                //提取我们想要的评论信息
                List<Comment> newComments = new ArrayList<>();
                for (Comment obj:comments){
                    Comment comment = new Comment();
                    BeanUtils.copyProperties(obj,comment,"id","tweetId","isDeleted");
                    newComments.add(comment);
                }

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

                //封装更多其他信息
                tweetDto.setAvatarUrl(avatar_url);
                tweetDto.setNickName(nickName);
                tweetDto.setUserName(userName);
                list.add(tweetDto);
            }

            pageDto.setRecords(list);
            return R.success(pageDto,null);

        }else {

            Page<Tweet> pageInfo = new Page<>(pageNum,20);
            Page<TweetDto> pageDto = new Page<>();
            LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
            //根据发忒时间降序排序
            queryWrapper.orderByDesc(Tweet::getCreateDate);
            tweetService.page(pageInfo,queryWrapper);

            BeanUtils.copyProperties(pageInfo,pageDto,"records");
            System.out.println();

            //用来封装推文信息和部分用户信息
            List<TweetDto> list = new ArrayList<>();

            List<Tweet> records = pageInfo.getRecords();
            for(Tweet item:records){
                TweetDto tweetDto = new TweetDto();
                BeanUtils.copyProperties(item,tweetDto);
                Long uid = item.getUid();
                Long tweetId = item.getTweetId();
                //查找指定用户的信息
                User user = userService.getById(uid);
                String nickName = user.getNickName();
                String avatar_url = user.getAvatarUrl();
                String userName = user.getUserName();

                //用户未登录，点赞状态永远为false
                tweetDto.setLikeStatus(false);

                //封装信息
                tweetDto.setAvatarUrl(avatar_url);
                tweetDto.setNickName(nickName);
                tweetDto.setUserName(userName);
                list.add(tweetDto);
            }

            pageDto.setRecords(list);

            return R.success(pageDto,null);

        }

    }

    /**
     * 获取某个用户的所有推文
     * @param userId
     * @return
     */
    @Override
    public List getUserTweet(Long userId) {

        LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tweet::getUid,userId)
                        .orderByDesc(Tweet::getCreateDate);

        List<Tweet> tweets = tweetService.getBaseMapper().selectList(queryWrapper);
        if (tweets.size()==0){
            return null;
        }

        return tweets;
    }
}
