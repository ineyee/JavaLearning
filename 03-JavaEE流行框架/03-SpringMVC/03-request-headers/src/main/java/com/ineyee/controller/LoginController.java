package com.ineyee.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

// 常见的请求头字段详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

@Controller
public class LoginController {
    @RequestMapping(path = "/login01", method = RequestMethod.POST)
    @ResponseBody
    // 方式一：使用 @RequestHeader 注解获取所有请求头
    public String login01(@RequestHeader Map<String, Object> headers) {
        return "login success, headers = " + headers;
    }

    @RequestMapping(path = "/login02", method = RequestMethod.POST)
    @ResponseBody
    // 方式二：使用 @RequestHeader 注解获取特定请求头
    //     value：指定请求头字段名称
    //     required：指定请求头字段是否必须，默认为 true
    public String login02(
            @RequestHeader("token") String token,
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        return "login success, token = " + token + ", userAgent = " + userAgent;
    }

    @RequestMapping(path = "/login03", method = RequestMethod.POST)
    @ResponseBody
    // 方式三：使用之前的 HttpServletRequest req 参数获取所有请求头
    public String login03(HttpServletRequest req) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = req.getHeader(name);
            headers.put(name, value);
        }

        return "login success, headers = " + headers;
    }

    @RequestMapping(path = "/login04", method = RequestMethod.POST)
    @ResponseBody
    // 方式四：使用之前的 HttpServletRequest req 参数获取特定请求头
    public String login04(HttpServletRequest req) {
        return "login success, token = " + req.getHeader("token") + ", userAgent = " + req.getHeader("User-Agent");
    }
}
