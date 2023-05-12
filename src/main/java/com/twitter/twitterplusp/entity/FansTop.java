package com.twitter.twitterplusp.entity;

import lombok.Data;

import java.util.List;

@Data
public class FansTop {

    private List<String> nickName;

    private List<Long> fansCount;

    private List<String> userAvatarUrl;

}
