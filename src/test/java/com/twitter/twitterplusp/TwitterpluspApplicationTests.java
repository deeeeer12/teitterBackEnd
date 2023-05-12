package com.twitter.twitterplusp;

import com.twitter.twitterplusp.service.TweetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TwitterpluspApplicationTests {

    @Autowired
    private TweetService tweetService;

    @Test
    void contextLoads() {

    }

}
