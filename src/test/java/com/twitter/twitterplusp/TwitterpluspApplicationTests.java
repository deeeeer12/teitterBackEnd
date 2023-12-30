package com.twitter.twitterplusp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.CommentMapper;
import com.twitter.twitterplusp.utils.Upload;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class TwitterpluspApplicationTests {

    @Test
    void contextLoads() throws Exception {

        //1.建立连接：使用Jsoup的connect()方法创建与目标网页的连接，并获取一个Connection对象。
        Connection connect = Jsoup.connect("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true")
                .header("User-Agent", "Mozilla/5.0")
                .timeout(10000)
                .ignoreContentType(true);

        //2.发送请求和接收响应：使用get()或post()方法发送HTTP请求并接收响应。可以设置请求头、参数等
        Connection.Response response = connect.method(Connection.Method.GET).execute();

        //3.解析和处理页面内容：使用Jsoup提供的API解析网页内容，例如获取元素、属性、文本等。可以使用CSS选择器或类似jQuery的语法进行选择
        Document document = response.parse();

        Elements url = document.select("url");

        String jsonString = JSON.toJSONString(url);
        System.out.println(jsonString);


    }

    @Test
    void test2() throws Exception {
        URL url = new URL("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();



        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Body: " + response.toString());
    }

    @Test
    void testTimestampToDate(){
        long l = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(1687331688576L);
        System.out.println(format);
    }

    @Test
    void testDateToTimestamp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2023-06-21 15:14:48");
        long time = date.getTime();
        System.out.println(time);
    }

}
