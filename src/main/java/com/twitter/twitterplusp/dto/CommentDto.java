package com.twitter.twitterplusp.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.twitter.twitterplusp.entity.Comment;
import com.twitter.twitterplusp.entity.User;
import lombok.Data;

@Data
public class CommentDto extends Comment {

    //评论人信息
    private String userName;

    private String nickName;

    private String avatarUrl;

    //逻辑删除字段
    @TableLogic
    private Integer isDeleted;

}