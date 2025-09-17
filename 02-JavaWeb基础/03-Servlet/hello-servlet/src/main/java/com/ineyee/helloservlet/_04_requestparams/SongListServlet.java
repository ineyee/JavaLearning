/**
 * 这个 Servlet 演示获取 get 请求和 post 请求的参数
 */

package com.ineyee.helloservlet._04_requestparams;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/songList04")
public class SongListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 我们只需要调用 req.getParameter(xxx) 这个方法一个参数一个参数获取就可以了
        // 不过需要注意的是：无论客户端传递参数的数据类型是什么，这个方法永远都是返回 String，所以可能需要手动转换一下数据类型
        String id = req.getParameter("id");
        String page = req.getParameter("page");
        String size = req.getParameter("size");

        int idNum = Integer.parseInt(id);
        int pageNum = Integer.parseInt(page);
        int sizeNum = Integer.parseInt(size);

        System.out.println("请求参数：" + idNum + " " + pageNum + " " + sizeNum);

        resp.getWriter().write("{\"code\":0,\"message\":\"获取歌单成功\",\"data\":{\"id\":" + idNum + ",\"page\":" + pageNum + ",\"size\":" + sizeNum + "}}");
    }
}
