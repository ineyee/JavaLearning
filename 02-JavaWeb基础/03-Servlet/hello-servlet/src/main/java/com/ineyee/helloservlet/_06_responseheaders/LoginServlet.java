/**
 * 这个 Servlet 演示写入响应头
 */

package com.ineyee.helloservlet._06_responseheaders;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login06")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 常见的响应头详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

        // 我们可以根据实际情况告诉客户端响应体的格式为 json（用的较多，要跟客户端约定好）、普通文本还是 html 文本
        // 现在响应体的编码方式普通默认就是 UTF-8
        resp.setContentType("application/json;charset=utf-8");
//        resp.setContentType("text/plain;charset=utf-8");
//        resp.setContentType("text/html;charset=utf-8");
    }
}
