package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @TableId("message_id")
    private Long messageId;//消息ID

    private Long senderId;//发送者ID

    private Long receiverId;//接受者ID

    private Long msgInfoId;//消息详细内容ID

    private Boolean status;//查看状态 1已查看，0未查看

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long createDate;//创建日期

}
