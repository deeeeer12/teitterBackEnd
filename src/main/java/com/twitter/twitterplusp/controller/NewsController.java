package com.twitter.twitterplusp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.utils.GetHotNewsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Iterator;


@Slf4j
@RestController
@RequestMapping("/teitter/v2/api/news")
public class NewsController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    GetHotNewsUtil getHotNewsUtil;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/getHot")
    public R getHotNews() throws IOException {

        String hot = getHotNewsUtil.getHot();

        if (!ObjectUtils.isEmpty(hot)){
            //转换为JSON格式进行响应
            JsonNode root = objectMapper.readTree(hot);
            JsonNode data = root.get("data");

            R r = new R();
            r.setData(data);
            r.setMsg("获取热点新闻成功");
            r.setStatus(200);
            return r;
        }

        return R.error("未获取到热点新闻");


    }
}
