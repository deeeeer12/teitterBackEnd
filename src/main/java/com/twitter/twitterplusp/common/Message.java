package com.twitter.twitterplusp.common;

import lombok.Data;

@Data
public class Message {
    private Long userId;
    private String message;
    private boolean sender;
    private String date;
    private Integer status;//已读状态 0未读，1已读
}
