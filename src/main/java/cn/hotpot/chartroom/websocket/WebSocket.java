package cn.hotpot.chartroom.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinzhu
 * @since 2019/12/18
 */
@ServerEndpoint("/chart/{userId}")
@Slf4j
@Component
public class WebSocket {

    private static Map<String, Session> map = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        log.debug("{}已进入聊天室", userId);
        map.put(userId, session);
    }

    @OnMessage
    public void sendMessage(Session session, String message) {
        map.forEach((k, v) -> {
            try {
                log.debug("发送消息：{}", message);
                v.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
