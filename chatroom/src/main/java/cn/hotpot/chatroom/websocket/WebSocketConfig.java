package cn.hotpot.chatroom.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author qinzhu
 * @since 2019/10/31
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter createServerEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
