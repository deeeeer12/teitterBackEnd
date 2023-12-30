package com.twitter.twitterplusp.config;

import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {


        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        User user = loginUser.getUser();
            attributes.put("user", user);

        System.out.println("*********************************************");
        System.out.println("*********************************************");
        System.out.println(user.toString());
        System.out.println("*********************************************");
        System.out.println("*********************************************");

        //测试
//        User user
//                 = new User();
//        user.setUid(1604482039181283330L);
//        user.setUserName("admin");
//        user.setNickName("管理员大爹");
//        attributes.put("user",user);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {

//        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
//        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
//        if (StringUtils.hasText(httpRequest.getHeader("Sec-WebSocket-Protocol"))) {
//            httpResponse.addHeader("Sec-WebSocket-Protocol", httpRequest.getHeader("Sec-WebSocket-Protocol"));
//        }
//        log.info("HandshakeInterceptor afterHandshake end...");
//

    }
}
