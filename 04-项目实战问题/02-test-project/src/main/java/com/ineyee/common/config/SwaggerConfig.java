package com.ineyee.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置类，用来配置接口文档的内容
 * 接口文档的访问地址：http://localhost:9999/tp-dev/swagger-ui/index.html
 * 接口文档 JSON 版访问地址：http://localhost:9999/tp-dev/v3/api-docs
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 接口文档的标题
                        .title("《项目实战问题》接口文档")
                        // 接口文档的版本
                        .version("1.0.0"));
    }
}