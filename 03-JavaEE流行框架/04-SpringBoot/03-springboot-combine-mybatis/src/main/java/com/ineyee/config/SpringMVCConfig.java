package com.ineyee.config;

import com.ineyee.interceptor.HttpLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {
    // 自动注入拿到 HTTP 请求日志拦截器
    @Autowired
    private HttpLogInterceptor httpLogInterceptor;

    // 添加拦截器
    //
    // 设置拦截器能拦截那些请求：这里的请求就是我们通过 / 设置了 DispatcherServlet 会拦截接口型请求 + 会拦截静态资源型请求这两种
    // 虽然 DispatcherServlet 把静态资源型请求转交给服务器默认的处理了，但拦截它肯定是会先拦截下来的，也就是说先拦截后转交，而不是直接转交
    // /*：代表 http://xxx/applicationContext/xxx 这种一级路径的请求才会被拦截
    // /**：代表 http://xxx/applicationContext/xxx、http://xxx/applicationContext/xxx/xxx 这种一级路径或 N 级路径的请求都会被拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLogInterceptor)
                .addPathPatterns("/**");
    }
}
