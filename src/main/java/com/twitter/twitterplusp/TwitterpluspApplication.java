package com.twitter.twitterplusp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.twitter.twitterplusp.mapper")
@SpringBootApplication
@EnableTransactionManagement//开启事务
public class TwitterpluspApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterpluspApplication.class, args);
    }

}
