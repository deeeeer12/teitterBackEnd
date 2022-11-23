package com.twitter.twitterplus.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class User {

    @TableId("uid")
    private Long uid;
    private String userName;
    private String userPassword;
    private String nickName;
    private String avatarUrl;
    private String birth;
    @TableLogic
    private int isDeleted;

}
