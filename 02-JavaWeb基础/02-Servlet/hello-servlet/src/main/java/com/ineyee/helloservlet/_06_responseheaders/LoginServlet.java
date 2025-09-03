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

        // 告诉客户端响应体的格式为 json 或普通文本，编码方式为 utf-8
        // 如果不设置这个 header 的话，浏览器默认是按照二进制数据来解析的，所以会乱码；其它客户端可能默认就是 utf-8 解码
//        resp.setContentType("application/json;charset=utf-8");
        resp.setContentType("text/plain;charset=utf-8");
    }
}
