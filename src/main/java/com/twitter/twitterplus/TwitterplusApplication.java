package com.twitter.twitterplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.twitter.twitterplus.mapper")
@SpringBootApplication
public class TwitterplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterplusApplication.class, args);
    }

}
