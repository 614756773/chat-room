package cn.hotpot.chartroom.websocket;

import cn.hotpot.chartroom.enums.WebsocketMeassageType;
import cn.hotpot.chartroom.model.dto.Message;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@ServerEndpoint("/chart/{userId}")
@Slf4j
@Component
public class WebSocket {

    private static Map<String, SessionUser> map = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        log.debug("{}已进入聊天室", userId);
        map.put(session.getId(), new SessionUser(userId, session));
    }

    @OnMessage
    public void receiveClientMessage(Session session, String message) {
        log.debug("用户{}说：{}", map.get(session.getId()).userId, message);
        sendToAllClient(message);
    }

    @OnClose
    public void onClose(Session session) {
        String id = session.getId();
        log.debug("{}离开聊天室", map.get(id).userId);
        map.remove(id);
    }

    private void sendToAllClient(String data) {
        Message msg = new Message()
                .setType(WebsocketMeassageType.GROUP_SENDING.getCode())
                .setData(data);
        map.forEach((k, v) -> {
            try {
                v.session.getBasicRemote().sendText(JSONObject.toJSONString(msg));
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
