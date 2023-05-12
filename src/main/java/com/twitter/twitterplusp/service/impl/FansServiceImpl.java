package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.Fans;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.FansMapper;
import com.twitter.twitterplusp.service.FansService;
import com.twitter.twitterplusp.service.FollowService;
import com.twitter.twitterplusp.service.UserService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import com.twitter.twitterplusp.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class FansServiceImpl extends ServiceImpl<FansMapper, Fans> implements FansService {

    @Autowired
    private FansService fansService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowServiceImpl followServiceImpl;

    @Override
    public TreeSet<Fans> getAllFans(Long uid) {

        Long loginUid = GetLoginUserInfo.getLoginUser().getUser().getUid();

        String key = "fans:"+uid;
        Set<Long> fansSet = redisCache.getCacheSet(key);

        //按照粉丝关注的时间进行排序
        TreeSet<Fans> treeSet = new TreeSet<>();

        if (fansSet != null){
            for(Long fanId:fansSet){
                Fans fans = new Fans();
                LambdaQueryWrapper<Fans> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Fans::getFansUserId,fanId)
                        .eq(Fans::getUid,uid);
                Fans one = fansService.getOne(queryWrapper);
                if (one!=null){
                    fans.setCreateDate(one.getCreateDate());
                }
                User user = userService.getById(fanId);

                if (user!=null){
                    String nickName = user.getNickName();
                    String avatarUrl = user.getAvatarUrl();
                    String userName = user.getUserName();
                    String profile = user.getProfile();
                    fans.setFansUserNickname(nickName);
                    fans.setFansUserAvatar(avatarUrl);
                    fans.setFansUsername(userName);
                    fans.setFansUserProfile(profile);
                    fans.setUid(fanId);
                }
                //添加两两关系
                List<Integer> integers = followServiceImpl.ptoPRelation(loginUid, fanId);
                if (integers!=null){
                    fans.setPToPRelation(integers);
                }
                if (fans!=null){
                    treeSet.add(fans);
                }
            }
            return treeSet;
        }else {
            return null;
        }

    }
}
