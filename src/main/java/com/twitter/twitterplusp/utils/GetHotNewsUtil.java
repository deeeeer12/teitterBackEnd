package com.twitter.twitterplusp.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * 获取热点新闻
 */
@Component
public class GetHotNewsUtil {

    @Autowired
    RedisCache redisCache;

    public String getHot() throws IOException {

        //先查询redis，看是否有热点新闻的缓存，若没有，则通过http去请求热点新闻数据，然后将其存入缓存中。
        String hotNews = redisCache.getCacheObject("HotNews");

        if (!ObjectUtils.isEmpty(hotNews)){
          return hotNews;
        }

        URL url = new URL("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        //将数据保存到redis中，以便查询，并且设置一小时过期时间
        redisCache.setCacheObject("HotNews",response.toString(),1,TimeUnit.HOURS);

        return response.toString();

    }

}

