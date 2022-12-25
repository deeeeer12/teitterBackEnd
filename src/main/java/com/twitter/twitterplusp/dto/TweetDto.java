package com.twitter.twitterplusp.dto;

import com.twitter.twitterplusp.entity.Tweet;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TweetDto extends Tweet {

    private String avatarUrl;

    private String nickName;

    private String userName;

    private boolean likeStatus;

    private List comments;

}
