package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.CommentMapper;
import com.twitter.twitterplusp.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentService commentService;

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
}
