package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_topic_tweet")
@Data
public class TopicAndTweet {

    private Long id;

    private Long topicId;//话题id

    private Long tweetId;//推文id

}
