package cn.hotpot.chatroom.websocket;

import cn.hotpot.chatroom.common.enums.WebsocketMeassageType;
import cn.hotpot.chatroom.model.dto.Message;
import cn.hotpot.chatroom.model.dto.MessageFrame;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
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
@ServerEndpoint("/chat/{userId}")
@Slf4j
@Component
public class WebSocketService {

    /**
     * key -> sessionId, value -> userId + Session
     */
    private static Map<String, SessionUser> map = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        log.debug("{}已进入聊天室", userId);
        map.put(session.getId(), new SessionUser(userId, session));
        sendOnlineNumber();
    }

    @OnMessage
    public void receiveClientMessage(Session session, String message) {
        log.debug("用户{}说：{}", map.get(session.getId()).userId, message);
        sendToAllClient(message, session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        String id = session.getId();
        log.debug("{}离开聊天室", map.get(id).userId);
        map.remove(id);
        sendOnlineNumber();
    }

    /**
     * 发送在线人数给客户端
     */
    private void sendOnlineNumber() {
        Message message = new Message();
        message.setType(WebsocketMeassageType.ONLINE_NUMBER.getCode());
        message.setContent(Integer.toString(map.size()));
        sendToAllClient(JSON.toJSONString(message), null);
    }

    private void sendToAllClient(String data, @Nullable String sessionId) {
        Message message = JSON.parseObject(data, Message.class);
        if (!WebsocketMeassageType.contains(message.getType())) {
            throw new RuntimeException("非法的消息类型");
        }
        map.forEach((k, v) -> {
            try {
                // 加上判断，不然还会发给自己- -。
                if (!k.equals(sessionId)) {
                    v.session.getBasicRemote().sendText(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @AllArgsConstructor
    private class SessionUser {
        String userId;
        Session session;
    }
}
