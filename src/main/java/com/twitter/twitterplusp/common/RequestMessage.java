package com.twitter.twitterplusp.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {
    private String message;
    private String from;//发送者nickName
    private String to;//接收者nickName
    private String content;
    private String nickName;
}
