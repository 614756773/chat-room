package cn.hotpot.chatroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinzhu
 * @since 2020/3/25
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createCommonRestApi() {
        return produceDocket("cn.hotpot.chatroom.controller");
    }

    private Docket produceDocket(String modulePackage) {
        List<Parameter> parameters = new ArrayList<>();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("聊天室接口文档")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(modulePackage))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters);
    }
}
