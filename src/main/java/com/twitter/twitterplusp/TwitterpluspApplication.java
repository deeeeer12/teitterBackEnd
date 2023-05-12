package com.twitter.twitterplusp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@MapperScan("com.twitter.twitterplusp.mapper")
@SpringBootApplication
@EnableTransactionManagement//开启事务
@EnableGlobalAuthentication//开启权限认证
public class TwitterpluspApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwitterpluspApplication.class, args);
    }

}
