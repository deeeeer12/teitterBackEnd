package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("tb_follow")
public class Follow implements Comparable<Follow>{

    private Long id;

    @TableField(value = "user_id")
    private Long uid;

//    @TableField(value = "follow_user_id")
    private Long followUserId;

    private String followUserNickname;//关注人昵称

    private String followUserAvatar;//关注人头像地址

    @TableField(exist = false)//不会映射数据库
    private String followsUsername;//粉丝用户名

    @TableField(exist = false)//不会映射数据库
    private String followsUserProfile;//粉丝的个人简介

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long createDate;

    @TableField(exist = false) //表明非数据库字段
    private List<Integer> pToPRelation;//两两用户之间的关联关系，[]双方都没有关注彼此，[1,2]互关，[1]已关注，[2]回关


    @Override
    public int compareTo(Follow o) {
        long l = o.createDate - this.createDate;
        int result = (int) l;
        return result;
    }
}
