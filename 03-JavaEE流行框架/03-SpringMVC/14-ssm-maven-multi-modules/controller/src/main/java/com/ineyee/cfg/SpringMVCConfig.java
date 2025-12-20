package com.ineyee.cfg;

import com.ineyee.interceptor.HttpLogInterceptor;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

// SpringMVC 配置类，实现 WebMvcConfigurer 接口
// 实现若干方法，在这些方法里做配置
@Configuration
// 使用注解来开发 SpringMVC 的话，得加上这个注解
@EnableWebMvc
/*
通过 @ComponentScan 注解告诉 SpringMVC 框架哪个包里的 Controller 类是通过注解实现 IoC 的
SpringMVC 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 子容器里

子容器需要扫描：
    1. controller 所在的包
    2. 全局异常处理器所在的包（@ControllerAdvice）
    3. 拦截器所在的包
 */
@ComponentScan("com.ineyee.controller")
@ComponentScan("com.ineyee.api.exception")
@ComponentScan("com.ineyee.interceptor")
public class SpringMVCConfig implements WebMvcConfigurer {
    /*
    相当于 XML 里 <mvc:default-servlet-handler/> + <mvc:annotation-driven/> 的用途

    DispatcherServlet 虽然拦截到了静态资源
    但是我们不让它处理，而是转交给默认的静态资源 Servlet 走服务器默认的处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // 配置消息转换器
    // 相当于 XML 里 <mvc:message-converters> + <mvc:annotation-driven> 的用途
    // Spring 7.0 使用新的 HttpMessageConverters.ServerBuilder API（配合 Jackson 3.x）
    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        /*
         * Spring 7.0 推荐使用 JsonMapper.builder() 来构建自定义的 JSON mapper
         * findAndAddModules() 会自动发现并注册 Jackson 模块，包括 JavaTimeModule（JSR310 支持已内置）
         *
         * Jackson 3.0 的重大改进：
         * - 默认将 LocalDateTime 序列化为 ISO-8601 字符串格式（不再是时间戳数组）
         * - JavaTimeModule 已内置在 jackson-databind 中，无需额外依赖
         * - 包名从 com.fasterxml.jackson 改为 tools.jackson
         */
        JsonMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()  // 自动发现并注册 Jackson 模块，包括 JavaTimeModule
                .build();

        // 配置 JSON 转换器，使用自定义的 JsonMapper
        // Spring 7.0 会自动配置字符集为 UTF-8（包括 String 和 JSON）
        builder.withJsonConverter(new JacksonJsonHttpMessageConverter(jsonMapper));
    }

    // 配置验证器，用于校验参数是否必传
    // 相当于 XML 里的 <mvc:annotation-driven> + <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    // 启用方法级别的验证
    // 相当于 XML 里的 <mvc:annotation-driven> + <bean class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor"/>
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /*
     * 配置文件上传解析器
     * 相当于 XML 里的 <bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"/>
     *
     * 注意：方法名必须是 multipartResolver（固定值）
     * SpringMVC 会通过这个名字去 IoC 容器中获取文件上传解析器来解析 multipart/form-data 请求
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    // 自动注入拿到 HTTP 请求日志拦截器
    @Autowired
    private HttpLogInterceptor httpLogInterceptor;

    // 添加拦截器
    //
    // 设置拦截器能拦截那些请求：这里的请求就是我们通过 / 设置了 DispatcherServlet 会拦截接口型请求 + 会拦截静态资源型请求这两种
    // 虽然 DispatcherServlet 把静态资源型请求转交给服务器默认的处理了，但拦截它肯定是会先拦截下来的，也就是说先拦截后转交，而不是直接转交
    // /*：代表 http://xxx/applicationContext/xxx 这种一级路径的请求才会被拦截
    // /**：代表 http://xxx/applicationContext/xxx、http://xxx/applicationContext/xxx/xxx 这种一级路径或 N 级路径的请求都会被拦截
    //
    // 如果我们把静态资源都放在 applicationContext/asset 目录下，这里就代表不要拦截 asset 及其子目录下的静态资源请求
    // 这样一来拦截器就是只拦截接口型请求了
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLogInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/asset/**");
    }
}
