package com.ineyee.common.config;

import com.ineyee.common.prop.CorsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 跨域处理相关配置
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Autowired
    CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // /**：代表对所有的请求进行跨域处理
        registry.addMapping("/**")
                // *：代表允许任意静态资源服务器的源跨域访问我们服务器上的接口
                // 不过实际开发中我们一般就是填写自己静态资源服务器的源
                .allowedOrigins(corsProperties.getAllowedOrigins())
                // *：代表允许任意请求方法（GET、POST、HEAD、PUT、DELETE、CONNECT、OPTIONS、TRACE、PATCH）跨域访问
                .allowedMethods("*")
                // *：代表允许前端携带任意请求头
                .allowedHeaders("*")
                // true：代表允许前端跨域携带 Cookie
                .allowCredentials(true);
    }
}
