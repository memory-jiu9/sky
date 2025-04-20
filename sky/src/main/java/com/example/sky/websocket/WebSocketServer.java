package com.example.sky.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{cid}")
@Slf4j
public class WebSocketServer {

    // 保存会话
    private static Map<String, Session> group = new HashMap<>();

    // 建立连接时触发
    @OnOpen
    public void onOpen(@PathParam("cid") String cid, Session session) {
        log.info("用户：{}，建立连接", cid);
        group.put(cid, session);
    }

    // 断开连接时触发
    @OnClose
    public void onClose(@PathParam("cid") String cid) {
        log.info("用户：{}，断开连接", cid);
        group.remove(cid);
    }

    // 客户端发送消息时触发
    @OnMessage
    public void onMessage(@PathParam("cid") String cid, String message) {
        log.info("用户：{}，发送消息：{}", cid, message);
    }

    // 群发
    public void sentAll(String message) {
        Collection<Session> sessions = group.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}