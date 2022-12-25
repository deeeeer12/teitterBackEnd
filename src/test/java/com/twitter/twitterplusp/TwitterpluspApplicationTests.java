package com.twitter.twitterplusp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TwitterpluspApplicationTests {

    @Test
    void contextLoads() {
        int target;
        int[] nums={3,2,4};
        int[] result = new int[2];
        target=6;
        System.out.print("[");
        labe:for(int i = 0;i<target;i++){
            for(int j = 1;j< nums.length;j++){
                if(nums[i]+nums[j]==target){
                    result[0] = i;
                    result[1] = j;
                    break labe;
                }
            }
        }
    }

}
