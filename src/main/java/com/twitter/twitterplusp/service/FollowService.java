package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.entity.Follow;

import java.util.List;
import java.util.TreeSet;

public interface FollowService extends IService<Follow> {

    /**
     * 关注/取消关注
     *
     * @param userId
     * @param isFollow
     * @return
     */
    String follow(Long userId, Integer isFollow);

    /**
     * 获取用户关注列表
     * @param uid
     * @return
     */
    TreeSet<Follow> getAllFollow(Long  uid);

    List<Integer> ptoPRelation(Long loginUid,Long otherUid);



}
