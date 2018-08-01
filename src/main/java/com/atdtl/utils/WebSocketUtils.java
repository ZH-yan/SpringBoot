package com.atdtl.utils;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @since 2018/8/1 9:42
 */
public class WebSocketUtils {

    /**
     *  模拟存储 websocket  session的使用
     */
    public static final Map<String, Session> LIVING_SESSIONS_CACHE = new ConcurrentHashMap<>();

    public static void sendMessageAll(String message) {
        LIVING_SESSIONS_CACHE.forEach((sessionId, session) -> sendMessage(session, message));
    }

    /**
     *  发送消息给指定用户
     *
     * @param session   用户session
     * @param message   发送消息内容
     */
    public static void sendMessage(Session session, String message) {
        if (session == null) {
            return;
        }
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return;
        }
        try {
            basic.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
