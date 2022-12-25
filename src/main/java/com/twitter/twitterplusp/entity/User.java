package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("t_user")

public class User {

    @TableId("uid")
    private Long uid;

    private String userName;

    private String userPassword;

    private String nickName;

    @TableField(fill  = FieldFill.INSERT)//插入时填充字段
    private Long createDate;

    private String avatarUrl;

    private Integer status;

    //逻辑删除字段
    @TableLogic
    private Integer isDeleted;


}
