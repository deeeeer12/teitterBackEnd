package com.twitter.twitterplusp.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类，服务端响应的数据最终都会封装称为此对象
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    private Integer status; //编码：200成功，400和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object,String msg) {
        R<T> r = new R<T>();
        r.data = object;
        r.msg = msg;
        r.status = 200;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.status = 400;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
