package com.twitter.twitterplusp.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED,reason="token已过期或非法")
public class TokenOverdueException extends RuntimeException {

        public TokenOverdueException(String msg){
            super(msg);
        }
}
