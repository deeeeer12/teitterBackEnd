//package com.twitter.twitterplusp.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//
//@Slf4j
//@Component
//@ServerEndpoint("/websocket/{userId}")
//public class WebSocketServer {
//
//
//    //与某个客户端的连接回话，需要通过它来给客户端发送数据
//    private Session session;
//
//    //用户ID
//    private Long userId;
//
//    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
//    //  注：底下WebSocket是当前类名
//    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<>();
//
//    private static ConcurrentHashMap<Long,Session> sessionPool = new ConcurrentHashMap<Long,Session>();
//
//    /**
//     * 连接成功调用此方法
//     * @param session
//     * @param userId
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam(value="userId")Long userId) {
//        try {
//            this.session = session;
//            this.userId = userId;
//            webSocketServers.add(this);
//            sessionPool.put(userId, session);
//            log.info("【websocket消息】有新的连接，总数为:"+webSocketServers.size());
//        } catch (Exception e) {
//        }
//    }
//
//    @OnClose
//    public void onClose(){
//        try {
//            webSocketServers.remove(this);
//            sessionPool.remove(this.userId);
//            log.info("【websocket消息】连接断开，总数为："+webSocketServers.size());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message
//     */
//    @OnMessage
//    public void onMessage(String message) {
//        log.info("【websocket消息】收到客户端消息:"+message);
//    }
//
//    /** 发送错误时的处理
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//
//        log.error("用户错误,原因:"+error.getMessage());
//        error.printStackTrace();
//    }
//
//
//    // 此为广播消息
//    public void sendAllMessage(String message) {
//        log.info("【websocket消息】广播消息:"+message);
//        for(WebSocketServer webSocket : webSocketServers) {
//            try {
//                if(webSocket.session.isOpen()) {
//                    webSocket.session.getAsyncRemote().sendText(message);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // 此为单点消息
//    public void sendOneMessage(String userId, String message) {
//        Session session = sessionPool.get(userId);
//        if (session != null&&session.isOpen()) {
//            try {
//                log.info("【websocket消息】 单点消息:"+message);
//                session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // 此为单点消息(多人)
//    public void sendMoreMessage(String[] userIds, String message) {
//        for(String userId:userIds) {
//            Session session = sessionPool.get(userId);
//            if (session != null&&session.isOpen()) {
//                try {
//                    log.info("【websocket消息】 单点消息:"+message);
//                    session.getAsyncRemote().sendText(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//}
//
