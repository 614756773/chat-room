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

//    /**
//     * 需要加上这个bean，不然会报错
//     * <a href="https://github.com/spring-projects/spring-boot/issues/12814"></a>
//     * "@EnableWebSocket with no further configuration might create a no op scheduler which will conflict with any other use of a task scheduler."
//     */
//    @Bean
//    public TaskScheduler taskScheduler() {
//        return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
//    }
}
