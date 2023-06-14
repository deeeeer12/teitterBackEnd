package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.TweetMapper;
import com.twitter.twitterplusp.service.*;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

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

    @Autowired
    private FansService fansService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageInfoService messageInfoService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicAndTweetService topicAndTweetService;


    /**
     * 发送推文
     *
     * @param tweet
     * @param topicName
     * @param loginUser
     * @return
     */
    public R send(Tweet tweet, String topicName, LoginUser loginUser) {

        if (loginUser == null) {
            return R.error("请先登录再发忒");
        }

        if (tweet.getContent() == "") {
            return R.error("内容不能为空");
        }

        tweet.setNickName(loginUser.getUser().getNickName());
        tweet.setUid(loginUser.getUser().getUid());
        tweet.setLevel(0);

        //保存推文到数据库的推文表
        tweetService.save(tweet);

        //如果该推文绑定了话题，则去更新推文&话题表sys_topic_tweet
        if (topicName!=null||!("".equals(topicName))){
            LambdaQueryWrapper<Topic> queryWrapperTopic = new LambdaQueryWrapper<>();
            queryWrapperTopic.eq(Topic::getTopicName,topicName);
            Topic topic = topicService.getOne(queryWrapperTopic);
            if (topic!=null){
                Long id = topic.getId();
                TopicAndTweet topicAndTweet = new TopicAndTweet();
                topicAndTweet.setTopicId(id);
                topicAndTweet.setTweetId(tweet.getTweetId());
                topicAndTweetService.save(topicAndTweet);
            }

        }

        //通知该用户的粉丝，该用户更新了推文。
        TreeSet<Fans> allFans = fansService.getAllFans(loginUser.getUser().getUid());
        if (allFans!=null){
            for(Fans fan:allFans){
                //获取刚发送的推文的自增Id
                Long tweetId = tweet.getTweetId();

                //向消息详情表中存储相关信息
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setTitle(null);
                LambdaQueryWrapper<Tweet> infoQueryWrapper = new LambdaQueryWrapper<>();
                infoQueryWrapper.eq(Tweet::getTweetId,tweetId);
                Tweet tweetServiceOne = tweetService.getOne(infoQueryWrapper);

                //只取文章内容的前十个字符
                String content = tweetServiceOne.getContent();
                if(content.length()>30){
                    content = content.substring(0,30);
                }
                messageInfo.setContent(content+"... ...");
                messageInfo.setMsgType(5);
                messageInfo.setTweetId(tweet.getTweetId());
                messageInfoService.save(messageInfo);
                Long msgInfoId = messageInfo.getMsgInfoId();

                //向通知表中添加数据
                Long uid = fan.getUid();
                Message msg = new Message();
                msg.setSenderId(loginUser.getUser().getUid());
                msg.setReceiverId(uid);
                msg.setMsgInfoId(msgInfoId);
                messageService.save(msg);
            }
        }

        return R.success(null,"发忒成功");

    }

    /**
     * 获取忒文首页
     *
     * @param pageNum
     * @return
     */
    @Override
    public R selectAllTwt(Integer pageNum, String keyWord) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (!"anonymousUser".equals(name)) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            Page<Tweet> pageInfo = new Page<>(pageNum, 50);
            Page<TweetDto> pageDto = new Page<>();

            LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
            //根据发忒时间降序排序，并进行模糊查询
            queryWrapper
                    .like(keyWord != null, Tweet::getContent, keyWord)
                    .eq(Tweet::getLevel,0)
                    .or()
                    .eq(Tweet::getLevel,0)
                    .like(keyWord != null, Tweet::getNickName, keyWord)
                    .orderByDesc(Tweet::getCreateDate);
            tweetService.page(pageInfo, queryWrapper);


            BeanUtils.copyProperties(pageInfo, pageDto, "records");

            //用来封装推文信息和部分用户信息
            List<TweetDto> list = new ArrayList<>();

            List<Tweet> records = pageInfo.getRecords();
            for (Tweet item : records) {
                TweetDto tweetDto = new TweetDto();
                Long uid = item.getUid();
                Long tweetId = item.getTweetId();
                BeanUtils.copyProperties(item, tweetDto);
                //查找指定用户的信息
                User user = userService.getById(uid);
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
                queryWrapperLike.eq(Like::getUid, loginUser.getUser().getUid())
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

            pageDto.setRecords(list);
            return R.success(pageDto, null);

        } else {

            Page<Tweet> pageInfo = new Page<>(pageNum, 20);
            Page<TweetDto> pageDto = new Page<>();
            LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
            //根据发忒时间降序排序
            queryWrapper
                    .like(keyWord != null, Tweet::getContent, keyWord)
                    .eq(Tweet::getLevel,0)
                    .or()
                    .like(keyWord != null, Tweet::getNickName, keyWord)
                    .eq(Tweet::getLevel,0)
                    .orderByDesc(Tweet::getCreateDate);
            tweetService.page(pageInfo, queryWrapper);

            BeanUtils.copyProperties(pageInfo, pageDto, "records");
            System.out.println();

            //用来封装推文信息和部分用户信息
            List<TweetDto> list = new ArrayList<>();

            List<Tweet> records = pageInfo.getRecords();
            for (Tweet item : records) {
                TweetDto tweetDto = new TweetDto();
                BeanUtils.copyProperties(item, tweetDto);
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
                tweetDto.setTweetId(tweetId);
                list.add(tweetDto);
            }

            pageDto.setRecords(list);

            return R.success(pageDto, null);

        }

    }

    /**
     * 获取某个用户的所有推文
     *
     * @param userId
     * @return
     */
    @Override
    public List getUserTweet(Long userId) {

        LoginUser loginUser = GetLoginUserInfo.getLoginUser();

        LambdaQueryWrapper<Tweet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tweet::getUid, userId)
                .orderByDesc(Tweet::getCreateDate);

        List<Tweet> tweets = tweetService.getBaseMapper().selectList(queryWrapper);
        if (tweets.size() == 0) {
            return null;
        }


        List<TweetDto> tweetDtoList = new ArrayList<>();

        BeanUtils.copyProperties(tweets,tweetDtoList);

        for(Tweet item:tweets){
            TweetDto tweetDto = new TweetDto();
            BeanUtils.copyProperties(item,tweetDto);
            Long uid = item.getUid();
            User user = userService.getById(uid);
            String userName = user.getUserName();
            String avatarUrl = user.getAvatarUrl();

            Long tweetId = item.getTweetId();
            //查找推文的点赞信息
            LambdaQueryWrapper<Like> queryWrapperLike = new LambdaQueryWrapper<>();
            queryWrapperLike.eq(Like::getUid, loginUser.getUser().getUid())
                    .eq(Like::getTweetId, tweetId);
            Like like = likeService.getOne(queryWrapperLike);

            //封装当前登录用户对应的推文的点赞信息
            if (Objects.isNull(like) || like.getStatus()==0) {
                tweetDto.setLikeStatus(false);
            } else if (like.getStatus() == 1) {
                tweetDto.setLikeStatus(true);
            }

            tweetDto.setUserName(userName);
            tweetDto.setAvatarUrl(avatarUrl);
            tweetDtoList.add(tweetDto);
        }

        return tweetDtoList;
    }

    @Override
    public Boolean delTweets(List<Long> ids) {

        boolean b = tweetService.removeByIds(ids);

        return b;
    }

    @Override
    public String delTweet(Long tweetId) {

        boolean result = tweetService.removeById(tweetId);
        if(result){
            return "删除成功";
        }
        return "删除失败";
    }
}
