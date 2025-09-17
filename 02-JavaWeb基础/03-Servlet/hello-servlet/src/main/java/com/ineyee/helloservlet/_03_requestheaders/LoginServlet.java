/**
 * 这个 Servlet 演示获取请求头
 */

package com.ineyee.helloservlet._03_requestheaders;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login03")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 常见的请求头字段详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

        // 表单提交：application/x-www-form-urlencoded
        // Json 提交：application/json（用的较多，要跟客户端约定好）
        // 文件上传：multipart/form-data
        System.out.println("获取请求体的格式：" + req.getContentType());

        // 现在请求体的编码方式普通默认就是 UTF-8
        System.out.println("获取请求体的编码方式：" + req.getCharacterEncoding());
    }
}
