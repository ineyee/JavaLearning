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

// 通过定义不同的 servlet 类和指定不同的路径来区分请求路径
@WebServlet("/songList02")
public class SongListServlet extends HttpServlet {
    // 如果是 get 请求那就调用 doGet 方法
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("{\"code\":0,\"message\":\"获取歌单成功\",\"data\":[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]}");
    }
}
