package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.*;
import com.twitter.twitterplusp.mapper.FansMapper;
import com.twitter.twitterplusp.mapper.FollowMapper;
import com.twitter.twitterplusp.service.*;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import com.twitter.twitterplusp.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    private FollowService followService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private FansService fansService;

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private MessageInfoService messageInfoService;

    @Autowired
    private MessageService messageService;

    @Override
    @Transactional
    public String follow(Long userId, Integer isFollow) {
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        //获取当前登录的用户ID
        Long uid = loginUser.getUser().getUid();

        String followKey = "follows:"+uid;
        String fanKey = "fans:"+userId;

        //1关注，否则执行取消关注即0
        if (isFollow == 1){


            //先从redis中查询该用户的关注列表，是否已经关注了此id
            Set<Long> cacheSet = redisCache.getCacheSet(followKey);
            for (Long id:cacheSet){
                if (id.equals(userId)){
                    return "你已经关注过该用户了";
                }
            }

            //向关注表保存数据tb_follow
            Follow follow = new Follow();
            follow.setUid(uid);
            follow.setFollowUserId(userId);
            boolean isSuccess = followService.save(follow);

            //向粉丝表保存数据tb_fans
            Fans fans = new Fans();
            fans.setUid(userId);
            fans.setFansUserId(uid);
            fansService.save(fans);


            if (isSuccess){
                //保存关注信息
                redisTemplate.opsForSet().add(followKey,userId.toString());

                //保存粉丝信息
                redisTemplate.opsForSet().add(fanKey,uid.toString());

                //将用户表中的关注数进行更新
                LambdaUpdateWrapper<User> followUpdateWrapper = new LambdaUpdateWrapper<>();
                followUpdateWrapper.eq(User::getUid,uid)
                        .setSql("follows_count = follows_count + 1");
                userService.update(followUpdateWrapper);

                //对应的另一个用户粉丝数量会被+1
                LambdaUpdateWrapper<User> fansUpdateWrapper = new LambdaUpdateWrapper<>();
                fansUpdateWrapper.eq(User::getUid,userId)
                        .setSql("`fans_count` = `fans_count` + 1");
                userService.update(fansUpdateWrapper);

                //以通知的方式告诉被关注人
                //1向通知详情表中存放信息
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("新增粉丝");
                messageInfo.setMsgType(3);
                messageInfoService.save(messageInfo);
                Long msgInfoId = messageInfo.getMsgInfoId();

                //2向通知表中存放基本信息
                Message message = new Message();
                message.setReceiverId(userId);
                message.setSenderId(uid);
                message.setMsgInfoId(msgInfoId);
                messageService.save(message);

                return "关注成功";
            }
        }else {
            //先从redis中查询该用户的关注列表，是否关注过该用户
            Set<Long> cacheSet = redisCache.getCacheSet(followKey);
            for (Long id : cacheSet){
                if (id.equals(userId)){

                    //从关注表中删除关注信息
                    LambdaQueryWrapper<Follow> folQueryWrapper = new LambdaQueryWrapper<>();
                    folQueryWrapper.eq(Follow::getFollowUserId,userId);
                    followService.remove(folQueryWrapper);

                    //从粉丝表中删除粉丝信息
                    LambdaQueryWrapper<Fans> fanQueryWrapper = new LambdaQueryWrapper<>();
                    fanQueryWrapper.eq(Fans::getUid,userId)
                            .eq(Fans::getFansUserId,uid);
                    fansService.remove(fanQueryWrapper);

                    //删除redis中的关注信息
                    Long remove = redisTemplate.opsForSet().remove(followKey, userId.toString());
                    log.info(remove.toString());

                    //删除redis中的粉丝信息
                    redisTemplate.opsForSet().remove(fanKey,uid.toString());

                    //将用户表中的关注数量follows_count-1，同时将被取消关注的用户的粉丝数量-1
                    LambdaUpdateWrapper<User> followUpdateWrapper = new LambdaUpdateWrapper<>();
                    followUpdateWrapper.eq(User::getUid,uid)
                            .setSql("`follows_count` = `follows_count` - 1");
                    userService.update(followUpdateWrapper);

                    LambdaUpdateWrapper<User> fansUpdateWrapper = new LambdaUpdateWrapper<>();
                    fansUpdateWrapper.eq(User::getUid,userId)
                            .setSql("`fans_count` = `fans_count` - 1");
                    userService.update(fansUpdateWrapper);
                    return "取消成功";
                }
            }
            return "你还未关注过该用户";
        }
        return "关注成功";

    }

    /**
     * 获取该用户的关注列表（关注人的昵称、头像、个签、用户名）
     * @param uid
     * @return
     */
    @Override
    public TreeSet<Follow> getAllFollow(Long uid) {

        Long loginUid = GetLoginUserInfo.getLoginUser().getUser().getUid();

        String key = "follows:"+uid;
        Set<Long> followSet = redisCache.getCacheSet(key);

        //创建一个按关注时间进行升序排序的集合
        TreeSet<Follow> treeSet = new TreeSet<>();

        for(Long followUserId:followSet){

            Follow follow = new Follow();

            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getFollowUserId,followUserId)
                    .eq(Follow::getUid,uid);
            Follow one = followService.getOne(queryWrapper);
            follow.setCreateDate(one.getCreateDate());

            //获取关注用户的其他信息
            User user = userService.getById(followUserId);
            String nickName = user.getNickName();
            String avatarUrl = user.getAvatarUrl();
            String userName = user.getUserName();
            String profile = user.getProfile();
            follow.setFollowUserNickname(nickName);
            follow.setFollowUserAvatar(avatarUrl);
            follow.setFollowsUserProfile(profile);
            follow.setFollowsUsername(userName);

            //获取两个用户之间的关系[]双方都没有关注彼此，[1,2]互关，[1]已关注，[2]回关
            List<Integer> integers = ptoPRelation(loginUid, followUserId);
            follow.setPToPRelation(integers);

            treeSet.add(follow);

        }
        return treeSet;

    }

    @Override
    public List<Integer> ptoPRelation(Long loginUid, Long otherUid) {

        List<Integer> result = fansMapper.getPpRelation(loginUid,otherUid);

        return result;
    }

}
