package com.atdtl.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static com.atdtl.utils.WebSocketUtils.LIVING_SESSIONS_CACHE;
import static com.atdtl.utils.WebSocketUtils.sendMessage;
import static com.atdtl.utils.WebSocketUtils.sendMessageAll;

/**
 *  聊天室
 *      Socket.onopen       连接时触发
 *      Socket.onmessage    客户端接收到服务器端数据时触发
 *      Socket.onerror      通信时发生错误时触发
 *      Socket.onclose      连接关闭时触发
 *
 * @author Administrator
 * @since 2018/8/1 10:01
 */
@RestController
@ServerEndpoint("/chat-room/{username}")
public class ChatRoomServerEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomServerEndpoint.class);

    @OnOpen
    public void openSession(@PathParam("username") String username, Session session) {
        LIVING_SESSIONS_CACHE.put(username,session);
        String message = "欢迎用户[" + username + "] 加入聊天室！";
        log.info(message);
        sendMessageAll(message);
    }

    @OnMessage
    public void onMessage(@PathParam("username")String username, String message) {
        log.info(message);
        sendMessageAll("用户[" + username + "] : " + message);
    }

    @OnClose
    public void onClose(@PathParam("username")String username, Session session) {
        // 当前的Session移除
        LIVING_SESSIONS_CACHE.remove(username);
        // 并且通知其他人当前用户已经离开聊天室
        sendMessageAll("用户[" + username + "] 已经离开聊天室了！");
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }

    @GetMapping("/chat-room/{sender}/to/{receive}")
    public void onMessage(@PathVariable("sender") String sender, @PathVariable("receive") String receive, String message) {
        sendMessage(LIVING_SESSIONS_CACHE.get(receive), "[" + sender + "]" + "-> [" + receive + "] : " + message);
    }

}
