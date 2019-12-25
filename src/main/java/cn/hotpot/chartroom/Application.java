package cn.hotpot.chartroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@SpringBootApplication
@EnableJpaAuditing
public class Application {
    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}
