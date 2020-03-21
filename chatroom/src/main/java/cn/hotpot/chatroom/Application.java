package cn.hotpot.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}
