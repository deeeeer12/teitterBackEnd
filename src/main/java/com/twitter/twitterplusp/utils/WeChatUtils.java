package com.glu.teitter_app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glu.teitter_app.common.HttpClientHelper;
import com.glu.teitter_app.entity.WeChatInfo;

import java.rmi.UnexpectedException;

public class WeChatUtils {

    public static final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    public static String getOpenid(String code) throws UnexpectedException {
        String url = new StringBuilder().append(LOGIN_URL)
                .append("?appid="+ WeChatInfo.APPID)
                .append("&secret="+WeChatInfo.SECRET)
                .append("&js_code="+code)
                .append("&grant_type=authorization_code")
                .toString();

        String result = HttpClientHelper.get(url);
        if(result == null ) {//请求失败
            throw new UnexpectedException("获取会话失败");
        }
        JSONObject jsonObj = JSON.parseObject(result);
        String openId = jsonObj.getString("openid");
        return openId;
    }

}
