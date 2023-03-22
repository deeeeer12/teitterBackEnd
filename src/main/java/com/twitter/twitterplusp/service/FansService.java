package com.twitter.twitterplusp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplusp.entity.Fans;

import java.util.TreeSet;

public interface FansService extends IService<Fans> {
    TreeSet<Fans> getAllFans(Long uid);
}
