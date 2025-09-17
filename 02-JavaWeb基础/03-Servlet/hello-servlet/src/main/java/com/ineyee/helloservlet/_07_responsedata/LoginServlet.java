/**
 * 这个 Servlet 演示写入响应数据
 */

package com.ineyee.helloservlet._07_responsedata;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login07")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        textHtml(req, resp);
//        textPlain(req, resp);
        json(req, resp);
    }

    // 响应 HTML 文本
    private void textHtml(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 告诉客户端响应体的格式为 HTML 文本，编码方式为 utf-8
        resp.setContentType("text/html;charset=utf-8");
        // 写数据
        resp.getWriter().write("<h1>登录成功<h1>");
    }

    // 响应普通文本
    private void textPlain(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 告诉客户端响应体的格式为普通文本，编码方式为 utf-8
        resp.setContentType("text/plain;charset=utf-8");
        // 写数据
        resp.getWriter().write("<h1>登录成功<h1>");
    }

    // 响应 JSON 格式的数据
    private void json(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 告诉客户端响应体的格式为 JSON，编码方式为 utf-8
        resp.setContentType("text/json;charset=utf-8");
        // 写数据
        resp.getWriter().write("{\"code\":0,\"message\":\"登录成功\",\"data\":{\"token\":\"1234567890\"}}");
    }
}
