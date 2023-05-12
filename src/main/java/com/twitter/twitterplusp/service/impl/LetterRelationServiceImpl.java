package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.LetterRelation;
import com.twitter.twitterplusp.mapper.LetterRelationMapper;
import com.twitter.twitterplusp.service.LetterRelationService;
import org.springframework.stereotype.Service;

@Service
public class LetterRelationServiceImpl extends ServiceImpl<LetterRelationMapper, LetterRelation> implements LetterRelationService {
}
