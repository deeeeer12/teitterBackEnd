package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "tb_fans")
public class Fans implements Comparable<Fans>{

    private Long id;

    @TableField(value = "user_id")
    private Long uid;

    //    @TableField(value = "follow_user_id")
    private Long fansUserId;

    private String fansUserNickname;//粉丝昵称

    private String fansUserAvatar;//粉丝头像地址

    @TableField(exist = false)//不会映射数据库
    private String fansUsername;//粉丝用户名

    @TableField(exist = false)//不会映射数据库
    private String fansUserProfile;//粉丝的个人简介

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long createDate;

    @TableField(exist = false)//不会映射数据库
    private List<Integer> pToPRelation; //两两关系，[]双方都没有关注彼此，[1,2]互关，[1]已关注，[2]回关


    @Override
    public int compareTo(Fans o) {
        if (o.createDate==null){
            return 400;
        }
        long l = o.createDate - this.createDate;
        int result = (int) l;
        return result;
    }
}
