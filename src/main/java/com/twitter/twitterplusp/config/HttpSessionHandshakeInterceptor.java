package com.twitter.twitterplusp.config;

import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Component
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User user = loginUser.getUser();
            attributes.put("user", user);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {}
}
