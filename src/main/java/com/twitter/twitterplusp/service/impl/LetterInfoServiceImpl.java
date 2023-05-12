package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.LetterInfo;
import com.twitter.twitterplusp.mapper.LetterInfoMapper;
import com.twitter.twitterplusp.service.LetterInfoService;
import org.springframework.stereotype.Service;

@Service
public class LetterInfoServiceImpl extends ServiceImpl<LetterInfoMapper, LetterInfo> implements LetterInfoService {
}
