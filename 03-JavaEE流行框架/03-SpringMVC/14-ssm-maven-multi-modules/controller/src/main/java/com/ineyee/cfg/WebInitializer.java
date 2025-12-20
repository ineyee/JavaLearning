package com.ineyee.cfg;

import com.ineyee.filter.CachedBodyFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.jspecify.annotations.Nullable;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// 创建一个 WebInitializer 类，继承 AbstractAnnotationConfigDispatcherServletInitializer 类
// 重写若干方法，在这些方法里做配置
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    // 1、指定 Spring 主配置类
    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // 2、指定 Spring 子配置类，for SpringMVC
    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[]{SpringMVCConfig.class};
    }

    // 配置主控制器可以拦截哪些请求，注意还需配合 SpringMVC 配置类里的 configureDefaultServletHandling 方法一起使用
    // 将 DispatcherServlet 的拦截模式设置为 "/"，这意味着 DispatcherServlet 会拦截接口型请求，会拦截静态资源型请求，不会拦截动态资源型请求（拦截 2 个）
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /*
     * 3、配置过滤器
     * 相当于 XML 里的 <filter> + <filter-mapping>
     *
     * 配置了两个过滤器：
     * 1. CharacterEncodingFilter: 字符编码过滤器，用来处理 HTTP 请求的字符编码
     * 2. CachedBodyFilter: 请求体缓存过滤器，用来包装请求使请求体可以被多次读取
     *
     * 过滤器会自动拦截所有请求（/* 模式），包括：接口型请求 + 静态资源请求 + 动态资源请求
     * 过滤器的执行顺序：数组中的顺序就是执行顺序
     */
    @Override
    protected Filter[] getServletFilters() {
        // 1、创建字符编码过滤器
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        // 设置编码为 UTF-8，UTF-8 是目前最通用的字符编码
        characterEncodingFilter.setEncoding("UTF-8");
        // 强制使用 UTF-8 编码来处理请求和响应
        // 相当于 XML 里的 <init-param><param-name>forceEncoding</param-name><param-value>true</param-value></init-param>
        characterEncodingFilter.setForceEncoding(true);

        // 2、创建请求体缓存过滤器
        // 因为 HTTP 请求体只能被读取一次，拦截器读取后 Controller 就无法读取了
        // 所以需要用这个过滤器在拦截器之前包装请求，使请求体可以被多次读取
        CachedBodyFilter cachedBodyFilter = new CachedBodyFilter();

        // 返回过滤器数组，执行顺序：characterEncodingFilter -> cachedBodyFilter
        return new Filter[]{characterEncodingFilter, cachedBodyFilter};
    }

    // 4、配置文件上传，相当于 XML 里的 <multipart-config>
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        /*
         * 创建 MultipartConfigElement 对象，配置文件上传参数
         *
         * 参数说明：
         * 1. location: 文件上传时的临时目录，默认是 Servlet 容器的临时目录
         *    - 文件上传时，数据会先写入到硬盘上的临时目录，而不是直接放到内存中
         *    - 这样可以防止上传大文件时占用过多内存
         *    - 上传完成后，再将文件移动到指定的目标目录
         *
         * 2. maxFileSize: 单个文件的最大字节数
         *    - 默认值是 -1，表示无限制
         *    - 如果上传的文件超过该大小限制，会抛出异常
         *    - 这里设置为 10485760 字节，即 10 MB
         *
         * 3. maxRequestSize: 一次上传请求中所有文件的总大小上限
         *    - 默认值是 -1，表示无限制
         *    - 如果上传的文件总大小超过该限制，会抛出异常
         *    - 这里设置为 20971520 字节，即 20 MB
         *
         * 4. fileSizeThreshold: 文件写入到临时文件之前，允许保存在内存中的临界值
         *    - 当文件大小超过该值时，才会写入硬盘临时目录；否则保存在内存中
         *    - 默认是 0，表示所有上传文件都直接写入硬盘临时目录
         */
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                "/tmp",           // location: 临时目录
                10485760,         // maxFileSize: 单个文件最大 10 MB
                20971520,         // maxRequestSize: 请求总大小最大 20 MB
                0                 // fileSizeThreshold: 内存临界值 0 字节
        );

        // 将文件上传配置应用到 DispatcherServlet
        registration.setMultipartConfig(multipartConfig);
    }
}
