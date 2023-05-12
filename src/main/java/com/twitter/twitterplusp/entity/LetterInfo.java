package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterInfo {

    private Long id;

    private Long sendUserId;//发送者uid

    private Long relationId;

    private String content;

    @TableField(fill  = FieldFill.INSERT)//插入时填充字段
    private Long createDate;

}
