/**
 * 这个 Servlet 演示获取请求头
 */

package com.ineyee.helloservlet._04_requestheaders;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login04")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 常见的请求头字段详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

        System.out.println("获取请求体的格式：" + req.getContentType());
        System.out.println("获取请求体的编码方式：" + req.getCharacterEncoding());
    }
}
