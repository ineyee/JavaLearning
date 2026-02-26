package com.ineyee.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置类，用来配置接口文档的内容
 * 接口文档的访问地址：http://localhost:9999/hmd-dev/swagger-ui/index.html
 * 接口文档 JSON 版的访问地址：http://localhost:9999/hmd-dev/v3/api-docs
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // 安全方案的名称，可以随便取，但 "Bearer Token" 见名知意
        String securitySchemeName = "Bearer Token";

        return new OpenAPI()
                .info(new Info()
                        // 接口文档的标题
                        .title("《项目实战问题》接口文档")
                        // 接口文档的版本
                        .version("1.0.0"))
                // 配置安全方案
                .components(new Components()
                        .addSecuritySchemes("Bearer Token", new SecurityScheme()
                                // 代表是基于 HTTP Header 的认证机制
                                .type(SecurityScheme.Type.HTTP)
                                // 代表是采用 Authorization: Bearer ${Token} 的认证方案
                                .scheme("bearer")
                                // 代表 Token 的格式是 JWT
                                .bearerFormat("JWT")))
                // 全局应用此安全方案，即默认所有接口都需要 Token
                // 而只在登录、注册、获取验证码等接口上用 @SecurityRequirements() 空注解覆盖这个全局配置，让这些接口不需要 Token
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}