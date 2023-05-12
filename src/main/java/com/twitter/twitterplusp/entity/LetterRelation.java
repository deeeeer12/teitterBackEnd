package com.twitter.twitterplusp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterRelation {

    @TableId("relation_id")
    private Long id;

    private Long sendUserId;

    private Long receiveUserId;

}
