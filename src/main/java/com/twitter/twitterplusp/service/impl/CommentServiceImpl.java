package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.dto.CommentDto;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.CommentMapper;
import com.twitter.twitterplusp.service.*;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageInfoService messageInfoService;



    /**
     * 添加评论推文
     * @param tweetId
     */
    @Override
    public void addComment(Long tweetId, User user,String content) {

        //根据推文id查询该推文的层级
        LambdaQueryWrapper<Tweet> queryTweet = new LambdaQueryWrapper<>();
        queryTweet.eq(Tweet::getTweetId,tweetId);
        Tweet one = tweetService.getOne(queryTweet);

        Tweet tweet = new Tweet();
        //为评论推文绑定其父推文
        tweet.setParentTweetId(tweetId);
        tweet.setContent(content);
        tweet.setUid(user.getUid());
        //level>=1,代表这是子推文（评论推文）
        //查询推文id，评论的level应在该推文层级的基础上进行+1，方便今后进行递归查询
        Integer level = one.getLevel();
        tweet.setLevel(level+1);
        tweet.setNickName(user.getNickName());
        tweetService.save(tweet);
        //添加评论成功后，将该推文的评论数量+1
        LambdaUpdateWrapper<Tweet> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("comment_count = comment_count + 1 ")
                        .eq(Tweet::getTweetId,tweetId);
        tweetService.update(null,updateWrapper);

        //向消息详情表中存储相关信息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setComment(tweet.getContent());
        messageInfo.setMsgType(2);
        messageInfo.setTweetId(tweetId);
        LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tweet::getTweetId,tweetId);
        Tweet parentTweet = tweetService.getOne(queryWrapper);
        String parentContent = parentTweet.getContent();
        if(parentContent.length()>30){
            parentContent = parentContent.substring(0,30);
        }
        messageInfo.setContent(parentContent+"... ...");
        messageInfoService.save(messageInfo);
        Long msgInfoId = messageInfo.getMsgInfoId();

        //向消息通知表中存储相关信息
        Message message = new Message();
        message.setSenderId(user.getUid());
        LambdaQueryWrapper<Tweet> qw = new LambdaQueryWrapper<>();
        qw.eq(Tweet::getTweetId,one.getTweetId());
        Tweet twt = tweetService.getOne(qw);
        message.setReceiverId(twt.getUid());
        message.setMsgInfoId(msgInfoId);
        messageService.save(message);
    }

    /**
     * 获取推文所有评论推文
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
            LoginUser loginUser = GetLoginUserInfo.getLoginUser();
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
        LambdaQueryWrapper<Tweet> queryWrapperComment = new LambdaQueryWrapper<>();
        queryWrapperComment.eq(Tweet::getParentTweetId,tweetId)
                .orderByDesc(Tweet::getCreateDate);

        List<Tweet> comments = tweetService.getBaseMapper().selectList(queryWrapperComment);

        //提取我们想要的评论信息,并添加评论对应的用户信息
        List<Tweet> newComments = new ArrayList<>();
        for (Tweet obj:comments){
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(obj,commentDto,"id","level");
            Long userId = obj.getUid();
            User userInfo = userService.getById(userId);
            BeanUtils.copyProperties(userInfo,commentDto,"createDate");

            //默认为false
            commentDto.setLikeStatus(false);

            //查询当前登录用户对该条评论的点赞信息
            if (!"anonymousUser".equals(name)){
                LoginUser loginUser = GetLoginUserInfo.getLoginUser();
                LambdaQueryWrapper<Like> queryLikeStatus = new LambdaQueryWrapper<>();
                queryLikeStatus.eq(Like::getTweetId,obj.getTweetId())
                        .eq(Like::getUid,loginUser.getUser().getUid())
                        .eq(Like::getStatus,1);

                Like likeStatus = likeService.getOne(queryLikeStatus);
                if (!Objects.isNull(likeStatus)){
                    commentDto.setLikeStatus(true);
                }
            }
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

    @Override
    public Map getCommentV2ByTweetId(Long tweetId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        //根据推文id查询推文内容
        LambdaQueryWrapper<Tweet> queryWrapperTweet = new LambdaQueryWrapper<>();
        queryWrapperTweet.eq(Tweet::getTweetId,tweetId);
        Tweet tweet = tweetService.getOne(queryWrapperTweet);

        TweetDto tweetDto = new TweetDto();

        BeanUtils.copyProperties(tweet,tweetDto);

        if (!"anonymousUser".equals(name)){
            LoginUser loginUser = GetLoginUserInfo.getLoginUser();
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

        //查询推文的所有子评论以及评论的评论
        List<Tweet> child = this.getChild(tweetId);


        return null;
    }

    /**
     * 调用该方法，递归获取推文的子推文
     * @param pid
     * @return
     */
    public List<Tweet> getChild(Long pid){
        BaseMapper<Tweet> baseMapper = tweetService.getBaseMapper();
        List<Tweet> childComments = baseMapper.selectList(new LambdaQueryWrapper<Tweet>().eq(Tweet::getParentTweetId, pid));
        childComments.stream().forEach(s->System.out.println(s));
        if (childComments!=null){
            for (Tweet childComment : childComments) {
                getChild(childComment.getParentTweetId());
            }

        }
        return null;
    }
}
