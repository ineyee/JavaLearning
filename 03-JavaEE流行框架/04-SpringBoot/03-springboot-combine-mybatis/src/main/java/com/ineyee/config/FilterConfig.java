package com.ineyee.config;

import com.ineyee.filter.CachedBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 过滤器配置类，只需要创建这个配置类就行了，其它地方不需要配置，SpringBoot 会自动完成其它工作
 * 用于注册自定义过滤器到 Spring 容器
 */
@Configuration
public class FilterConfig {
    /**
     * 注册请求体缓存过滤器
     * 当 Spring 容器启动时，Spring 会调用这个方法，拿到返回的 FilterRegistrationBean，自动注册到 Servlet 容器中
     */
    @Bean
    public FilterRegistrationBean<CachedBodyFilter> cachedBodyFilter() {
        FilterRegistrationBean<CachedBodyFilter> registration = new FilterRegistrationBean<>();

        // 设置过滤器实例
        registration.setFilter(new CachedBodyFilter());

        // 设置拦截路径
        // /* 拦截所有请求：接口型请求 + 静态资源请求 + 动态资源请求
        registration.addUrlPatterns("/*");

        // 设置过滤器执行顺序（非常重要！）
        // HIGHEST_PRECEDENCE = Integer.MIN_VALUE，表示最高优先级，最先执行
        // 确保在 HTTP 请求日志拦截器之前包装请求
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        // 设置过滤器名称（可选，用于日志和调试）
        registration.setName("cachedBodyFilter");

        return registration;
    }
}