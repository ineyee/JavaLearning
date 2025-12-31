package com.ineyee.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

// 第一步：自定义一个类，实现 HandlerInterceptor 接口
// 第二步：重写该接口的三个方法
// 把我们自定义的 HTTP 请求日志拦截器放到 IoC 容器里
@Component
public class HttpLogInterceptor implements HandlerInterceptor {
    // 这个方法会在 controller 里接口方法执行前调用，返回 true 表示让请求继续往下执行，返回 false 表示让请求终止在这里
    // 当有多个拦截器时，多个拦截器的该方法会按照拦截器的注册顺序正序执行
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 记录请求开始时间
        request.setAttribute("startTime", System.currentTimeMillis());

        System.out.println("\n====================== 请求数据 ======================HttpLogInterceptor.preHandle begin");

        // method
        System.out.println("method = " + request.getMethod());

        // url
        System.out.println("url = " + request.getRequestURL().toString());

        // headers
        StringBuilder headerJson = new StringBuilder("{");
        Enumeration<String> headerNames = request.getHeaderNames();
        boolean firstHeader = true;
        while (headerNames.hasMoreElements()) {
            if (!firstHeader) headerJson.append(", ");
            firstHeader = false;
            String name = headerNames.nextElement();
            headerJson.append("\"").append(name).append("\": \"").append(request.getHeader(name)).append("\"");
        }
        headerJson.append("}");
        String headers = headerJson.toString();
        System.out.println("headers = " + (headers.equals("{}") ? null : headers));

        // params
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder paramJson = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            if (!first) paramJson.append(", ");
            first = false;
            paramJson.append("\"").append(entry.getKey()).append("\": ");
            if (entry.getValue().length == 1) {
                paramJson.append("\"").append(entry.getValue()[0]).append("\"");
            } else {
                paramJson.append("[");
                for (int i = 0; i < entry.getValue().length; i++) {
                    if (i > 0) paramJson.append(", ");
                    paramJson.append("\"").append(entry.getValue()[i]).append("\"");
                }
                paramJson.append("]");
            }
        }
        paramJson.append("}");
        String params = paramJson.toString();
        System.out.println("params = " + (params.equals("{}") ? null : params));

        // body - 先打印占位符，实际内容在 postHandle 中打印（因为 ContentCachingRequestWrapper 需要在内容被读取后才会缓存）
        System.out.println("body = [将在请求处理后显示]");

        System.out.println("====================== 请求数据 ======================HttpLogInterceptor.preHandle end\n");

        return true;
    }

    // 这个方法会在 controller 里接口方法执行后调用（接口方法 return 执行完毕后,Spring 框架会拿着返回值做一些事情,然后服务器才会给客户端发出去响应）
    // 当有多个拦截器时，多个拦截器的该方法会按照拦截器的注册顺序倒序执行
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 注意；
        // 如果 controller 里接口方法抛异常，该方法不会执行，但是 afterCompletion 方法依然会执行
        // 所以响应日志我们给它放到 afterCompletion 方法里记录，以保证异常情况下也能记录

        // 在这里打印请求体内容（此时 Spring MVC 已经读取并缓存了请求体）
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String body = new String(buf, 0, buf.length, StandardCharsets.UTF_8)
                        .replaceAll("\\s+", " ")
                        .trim()
                        .replaceAll("^\\{\\s+", "{");
                System.out.println("\n====================== 请求体内容 ======================");
                System.out.println("body = " + body);
                System.out.println("====================== 请求体内容 ======================\n");
            }
        }
    }

    // 这个方法会在服务器给客户端发出去响应后调用，至于客户端收没收到就管不了了
    // 当有多个拦截器时，多个拦截器的该方法会按照拦截器的注册顺序倒序执行
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        if (ex == null) {
            System.out.println("\n====================== 响应数据 ======================HttpLogInterceptor.afterCompletion begin");

            // url
            System.out.println("response for url = " + request.getRequestURL().toString());

            // statusCode
            System.out.println("statusCode = " + response.getStatus());

            // headers
            StringBuilder responseHeaderJson = new StringBuilder("{");
            boolean firstHeader = true;
            for (String headerName : response.getHeaderNames()) {
                if (!firstHeader) responseHeaderJson.append(", ");
                firstHeader = false;
                responseHeaderJson.append("\"").append(headerName).append("\": \"").append(response.getHeader(headerName)).append("\"");
            }
            responseHeaderJson.append("}");
            String responseHeaders = responseHeaderJson.toString();
            System.out.println("headers = " + (responseHeaders.equals("{}") ? null : responseHeaders));

            // 耗时
            Long startTime = (Long) request.getAttribute("startTime");
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                System.out.println("duration = " + duration + "ms");
            }

            System.out.println("====================== 响应数据 ======================HttpLogInterceptor.afterCompletion end\n");
        } else {
            System.out.println("\n====================== Controller 里接口方法抛出了异常 ======================HttpLogInterceptor.afterCompletion begin");

            // url
            System.out.println("response for url = " + request.getRequestURL().toString());

            // 组合异常信息为 JSON 字符串
            StringBuilder exceptionJson = new StringBuilder("{");
            exceptionJson.append("\"exception_type\": \"").append(ex.getClass().getName()).append("\", ");
            // 对异常消息进行简单的转义处理（转义双引号和反斜杠）
            String message = ex.getMessage() != null ? ex.getMessage().replace("\\", "\\\\").replace("\"", "\\\"") : "null";
            exceptionJson.append("\"exception_message\": \"").append(message).append("\"");
            exceptionJson.append("}");
            System.out.println("exception = " + exceptionJson);

            System.out.println("====================== Controller 里接口方法抛出了异常 ======================HttpLogInterceptor.afterCompletion end\n");
        }
    }
}
