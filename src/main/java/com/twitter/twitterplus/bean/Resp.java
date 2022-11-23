package com.twitter.twitterplus.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
    class Resp<E> {
    private String code;
    private String message;
    private E body;

    public <E> Resp<E> success(E body){

        return new Resp("200","成功",body);

    }

    public <E> Resp<E> fail(String code,String message){
        return new Resp(code,message,(Object)null);
    }
}
