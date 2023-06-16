package com.twitter.twitterplusp.config;

import com.twitter.twitterplusp.controller.ChatController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebMvcConfigurer, WebSocketConfigurer {


    @Autowired
    private com.twitter.twitterplusp.config.HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor;

    @Autowired
    private ChatController chatController;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(chatController,"/teitter/v2/api/intoChat")
                .addInterceptors(httpSessionHandshakeInterceptor);
    }

}
