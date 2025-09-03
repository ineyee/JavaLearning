/**
 * 这个 Servlet 演示区分不同的请求路径和请求方法
 */

package com.ineyee.helloservlet._02_requestpathmethod;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet("/login")
// 通过定义不同的 servlet 类和指定不同的路径来区分请求路径

public class LoginServlet extends HttpServlet {
    // 如果是 post 请求那就调用 doPost 方法
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("{\"code\":0,\"message\":\"登录成功\",\"data\":{\"token\":\"1234567890\"}}");
    }
}
