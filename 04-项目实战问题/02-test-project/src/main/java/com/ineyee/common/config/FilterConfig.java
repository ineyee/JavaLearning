package com.ineyee.common.config;

import com.ineyee.common.filter.JwtTokenFilter;
import com.ineyee.common.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 过滤器配置类，只需要创建这个配置类就行了，其它地方不需要配置，SpringBoot 会自动完成其它工作
 * 用于注册自定义过滤器到 Spring 容器
 */
@Configuration
public class FilterConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // JWT token 统一验证过滤器（跟 TokenFilter 是两套不同的验证方案，实际开发中用其中一个即可）
//    @Bean
//    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilter() {
//        FilterRegistrationBean<JwtTokenFilter> registration =
//                new FilterRegistrationBean<>();
//        // 设置过滤器实例
//        registration.setFilter(new JwtTokenFilter());
//        // 设置拦截路径
//        // /* 拦截所有请求：接口型请求 + 静态资源请求 + 动态资源请求
//        registration.addUrlPatterns("/*");
//        // 设置优先级，优先于
//        registration.setOrder(1);
//        // 设置过滤器名称（可选，用于日志和调试）
//        registration.setName("JwtTokenFilter");
//        return registration;
//    }

    // Redis token 统一验证过滤器（跟 JwtTokenFilter 是两套不同的验证方案，实际开发中用其中一个即可）
    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilter() {
        FilterRegistrationBean<TokenFilter> registration =
                new FilterRegistrationBean<>();
        registration.setFilter(new TokenFilter(redisTemplate));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("TokenFilter");
        return registration;
    }
}
