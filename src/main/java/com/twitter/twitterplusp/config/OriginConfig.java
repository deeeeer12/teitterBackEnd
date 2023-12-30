package com.twitter.twitterplusp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OriginConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOriginPatterns("https://teitter.cuijunyu.win")
                .allowedOrigins("wss://www.heron.love:8070/teitter/v2/api/intoChat")
//                .allowedOriginPatterns("wss://www.heron.love:8070")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders("*");

    }
}
