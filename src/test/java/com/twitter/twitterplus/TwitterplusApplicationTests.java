package com.twitter.twitterplus;

import com.baomidou.mybatisplus.annotation.TableName;
import com.twitter.twitterplus.bean.User;
import com.twitter.twitterplus.mapper.UserMapper;
import com.twitter.twitterplus.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class TwitterplusApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testConnection(){
        User user = new User();
        user.setUserName("风中追风");
        user.setUserPassword("821552133");
        int result = userMapper.insert(user);
        System.out.println(result);
    }

    @Test
    public void testDelete(){
        int result = userMapper.deleteById(1592724383804239874L);
        System.out.println(result);
    }



}
