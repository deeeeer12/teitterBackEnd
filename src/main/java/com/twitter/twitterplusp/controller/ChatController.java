package com.twitter.twitterplusp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.twitterplusp.common.Message;
import com.twitter.twitterplusp.common.RequestMessage;
import com.twitter.twitterplusp.common.ResponseMessage;
import com.twitter.twitterplusp.entity.LetterInfo;
import com.twitter.twitterplusp.entity.LetterRelation;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.service.LetterInfoService;
import com.twitter.twitterplusp.service.LetterRelationService;
import com.twitter.twitterplusp.service.UserService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import com.twitter.twitterplusp.utils.OnlineUserManager;
import com.twitter.twitterplusp.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ChatController extends TextWebSocketHandler {

    @Autowired
    UserService userService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    LetterRelationService relationService;

    @Autowired
    LetterInfoService letterInfoService;

    @Autowired
    OnlineUserManager onlineUserManager;


    //JSON转换
    private ObjectMapper mapper = new ObjectMapper();

    //连接成功时调用
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage();
        // 1. 首先判断当前用户是否已经登录, 防止用户多开

        User user = (User) session.getAttributes().get("user");

        if(onlineUserManager.getState(user.getUid()) != null) {
            responseMessage.setStatus(400);
            responseMessage.setMessage("当前用户已经登录了, 不要重复登录");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(responseMessage)));
            return;
        }
        // 2. 将用户的在线状态设置为在线
        onlineUserManager.enterHall(user.getUid(),session);
        // 3. 从数据库中查找所有聊过天的人
        // 4. 设置响应类, 并添加对应的信息
        responseMessage.setStatus(200);
        responseMessage.setMessage("getChats");

        //当前登录用户的uid
        Long uid = user.getUid();
        System.out.println(uid);
        //调用封装方法，查询所有聊过天的用户
        List<User> allUsers = getAllUsers(uid);
        //聊过天的人
        responseMessage.setUsers(allUsers);
        responseMessage.setSendUsernickname(user.getNickName());
        // 5. 返回响应
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(responseMessage)));
        System.out.println("用户"+user.getNickName()+"连接");

    }

    //连接成功后收到的响应
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        User user = (User) session.getAttributes().get("user");
        //1解析请求的内容
        String payload = message.getPayload();
        System.out.println(payload);
        RequestMessage requestMessage = mapper.readValue(payload, RequestMessage.class);
        //2判断是加载消息还是发送消息
        if ("loadMessage".equals(requestMessage.getMessage())){
            //2.a.1根据两者的id（sendUserId、receiveUserId）查找relationId
            responseMessage.setMessage("loadMessage");
            responseMessage.setReceiveUsernickname(requestMessage.getTo());

            //调用方法，获取uid
            Map<String, Long> uidByNickName = getUidByNickName(requestMessage.getFrom(), requestMessage.getTo());
            Long fromId = uidByNickName.get("fromId");
            Long toId = uidByNickName.get("toId");

            LambdaQueryWrapper<LetterRelation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LetterRelation::getSendUserId,fromId)
                        .eq(LetterRelation::getReceiveUserId,toId)
                    .or()
                        .eq(LetterRelation::getSendUserId,toId)
                        .eq(LetterRelation::getReceiveUserId,fromId);
            LetterRelation one = relationService.getOne(queryWrapper);
            //relationId即两个用户的聊天id，用来去letter_info表中查询他们的聊天记录
            //2.a.2 判断当前的relationId是否存在, 不存在就不需要加载聊天记录了
            if (one == null || "".equals(one)){
                responseMessage.setStatus(200);
                responseMessage.setMessages(null);//给聊天记录赋null
            }else {
                //2.a.3存在聊天记录，需要加载
                Long relationId = one.getId();
                responseMessage.setStatus(200);
                BaseMapper<LetterInfo> baseMapper = letterInfoService.getBaseMapper();
                LambdaQueryWrapper<LetterInfo> queryWrapperChatInfo = new LambdaQueryWrapper<>();
                queryWrapperChatInfo.eq(LetterInfo::getRelationId,relationId)
                        .orderByDesc(LetterInfo::getCreateDate);//根据消息时间降序排序，集合最上边的消息是最新的
                List<LetterInfo> letterInfos = baseMapper.selectList(queryWrapperChatInfo);
                List<Message> messages = new ArrayList<>();
                for (LetterInfo letterInfo : letterInfos) {
                    Message message1 = new Message();
                    message1.setMessage(letterInfo.getContent());//设置上该条消息的内容
                    message1.setUserId(letterInfo.getSendUserId());//设置当前消息是谁发送的
                    message1.setSender(user.getUid().equals(letterInfo.getSendUserId()));//判断当前登录用户是否为该条消息的发送者
                    messages.add(message1);
                }
                responseMessage.setMessages(messages);
            }
            //2.a.4设置对应的响应，并返回给前端
            session.sendMessage(new TextMessage(mapper.writeValueAsBytes(responseMessage)));
        }
        if("sendMessage".equals(requestMessage.getMessage())){
            //2.b.1查找两用户的relationId
            String fromNickname = requestMessage.getFrom();//从请求中获取发送者昵称
            String toNickname = requestMessage.getTo();//从请求中获取接收者昵称

            //调用方法，获取uid
            Map<String, Long> uidByNickName = getUidByNickName(fromNickname, toNickname);
            Long fromId = uidByNickName.get("fromId");
            Long toId = uidByNickName.get("toId");

            //根据两者的uid去letter_relation表中查询relationId
            LambdaQueryWrapper<LetterRelation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LetterRelation::getSendUserId,fromId)
                            .eq(LetterRelation::getReceiveUserId,toId);
            LetterRelation letterRelationEntity = relationService.getOne(queryWrapper);
            Long relationId = null;
            //2.b.2判断当前的relationId是否为空，若为空则在letter_relation表中创建该聊天关系，然后再获取其relationId
            if (letterRelationEntity == null){
                responseMessage.setStatus(200);
                LetterRelation letterRelation = new LetterRelation(null,fromId,toId);
                relationService.save(letterRelation);
                letterRelationEntity= relationService.getOne(queryWrapper);
                relationId = letterRelationEntity.getId();
            }
            //2.b.3根据relationId，在聊天表里添加数据
            String content = requestMessage.getContent();
            LetterInfo letterInfo = new LetterInfo(null,fromId,letterRelationEntity.getId(),content,null);
            letterInfoService.save(letterInfo);
            //2.b.4设置对应的响应信息
            responseMessage.setStatus(200);
            responseMessage.setMessage("sendMessage");
            //2.b.5获取两者用户的session, 并判断是否在线, 给在线的用户返回响应, 刷新聊天框
            Message message1 = new Message();
            WebSocketSession session1 = onlineUserManager.getState(fromId);
            WebSocketSession session2 = onlineUserManager.getState(toId);
            if (session1!=null){
                message1.setSender(true);
                message1.setMessage(content);
                message1.setUserId(fromId);
                List<Message> list = new ArrayList<>();
                list.add(message1);
                responseMessage.setMessages(list);
                session1.sendMessage(new TextMessage(mapper.writeValueAsBytes(responseMessage)));
            }
            if (session2!=null){
                message1.setSender(false);
                message1.setMessage(content);
                message1.setUserId(toId);
                List<Message> list = new ArrayList<>();
                list.add(message1);
                responseMessage.setMessages(list);
                session2.sendMessage(new TextMessage(mapper.writeValueAsBytes(responseMessage)));
            }
        }
    }

    // 连接异常调用
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        User user = (User) session.getAttributes().get("user");
        WebSocketSession webSocketSession = onlineUserManager.getState(user.getUid());
        if(webSocketSession == session) {
            // 2. 设置在线状态
            onlineUserManager.exitHall(user.getUid());
        }
        System.out.println("用户"+user.getNickName()+"退出");
    }

    // 连接关闭调用
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");
        WebSocketSession webSocketSession = onlineUserManager.getState(user.getUid());
        if(webSocketSession == session) {
            // 2. 设置在线状态
            onlineUserManager.exitHall(user.getUid());
        }
        System.out.println("用户"+user.getNickName()+"退出");
    }

    /**
     * 调用该方法，根据当前用户id获取与其聊过天的用户的详细信息
     * @param uid
     * @return
     */
    public List<User> getAllUsers(Long uid){
        List<User> users = new ArrayList<>();
        BaseMapper<LetterRelation> baseMapper = relationService.getBaseMapper();
        LambdaQueryWrapper<LetterRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LetterRelation::getSendUserId,uid)
                        .or().eq(LetterRelation::getReceiveUserId,uid);
        List<LetterRelation> letterRelations = baseMapper.selectList(queryWrapper);

        //用来存放与当前用户有过聊天的其他用户的id
        List<Long> ids = new ArrayList<>();
        for (LetterRelation letterRelation : letterRelations) {
            Long sendUserId = letterRelation.getSendUserId();
            Long receiveUserId = letterRelation.getReceiveUserId();
            if (!sendUserId.equals(uid)){
                ids.add(sendUserId);
            }
            if (!receiveUserId.equals(uid)){
                ids.add(receiveUserId);
            }
        }

        //循环遍历，获取这些用户的详细信息
        for (Long id : ids) {
            User user = userService.getById(id);
            users.add(user);
        }

        return users;
    }

    public Map<String,Long> getUidByNickName(String from,String to){

        Map<String,Long> map = new HashMap<>();

        //根据昵称分别获得发送者和接收者的uid
        LambdaQueryWrapper<User> queryWrapperFrom = new LambdaQueryWrapper<>();
        queryWrapperFrom.eq(User::getNickName,from);
        User fromUser = userService.getOne(queryWrapperFrom);
        Long sendUserId = fromUser.getUid();

        LambdaQueryWrapper<User> queryWrapperTo = new LambdaQueryWrapper<>();
        queryWrapperTo.eq(User::getNickName,to);
        User toUser = userService.getOne(queryWrapperTo);
        Long receiveUserId = toUser.getUid();

        map.put("fromId",sendUserId);
        map.put("toId",receiveUserId);

        return map;

    }

}
