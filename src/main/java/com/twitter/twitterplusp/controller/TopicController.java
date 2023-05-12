package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.dto.TweetDto;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.Tweet;
import com.twitter.twitterplusp.service.TopicService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teitter/api/topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    /**
     * 发布话题
     * @param topicName
     * @return
     */
    @PostMapping("/postTopic")
    public R postTopic(String topicName){
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        Long uid = loginUser.getUser().getUid();
        String result = topicService.postTopic(uid,topicName);
        if ("发布成功".equals(result)){
            return R.success(null,"发布话题成功");
        }else {
            return R.error("发布失败");
        }
    }

    /**
     * 根据id删除话题
     * @param id
     * @return
     */
    @PostMapping("/delTopic")
    public R delTopic(Long id){
        String result = topicService.delTopic(id);
        if ("删除失败".equals(result)){
            return R.error("删除失败");
        }
        return R.success(null,"删除成功");
    }

    /**
     * 根据话题id查找该话题下的所有推文，分页
     * @return
     */
    @GetMapping("/getTweetsByTopicId")
    public R getTweetsByTopicId(@RequestParam Long topicId){
        List<TweetDto> result = topicService.getTweetsByTopicId(topicId);

        return R.success(result,"成功");
    }

}
