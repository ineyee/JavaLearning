package com.ineyee.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

// 请求体只能被读取一次
// 因为 HTTP 请求日志拦截器里已经读取过请求体，所以就会导致 controller 里不能再次读取请求体而报错
//
// 因此这里创建一个过滤器，在请求到达 HTTP 请求日志拦截器之前包装请求
// 让 HTTP 请求日志拦截器读取包装过的请求，这样 controller 里就能正常读取请求体了
public class CachedBodyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 只包装 HTTP 请求
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // 使用 ContentCachingRequestWrapper 包装请求，它会缓存请求体内容
            // 第二个参数是缓存大小限制（字节），这里设置为 1MB
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest, 1024 * 1024);
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}