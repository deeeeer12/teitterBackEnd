package com.twitter.twitterplusp.common;

import lombok.Data;

@Data
public class Message {
    public Long UserId;
    public String message;
    public boolean isSender;
}
