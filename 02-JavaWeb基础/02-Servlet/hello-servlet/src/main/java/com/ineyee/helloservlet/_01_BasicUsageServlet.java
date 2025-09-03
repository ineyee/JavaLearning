/**
 * 这个 Servlet 演示基本使用
 */

package com.ineyee.helloservlet;

// 第一步：导入 servlet 相关的包
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 第二步：创建一个 servlet 类，继承自 HttpServlet
// 约定俗成地我们会把这个类实例命名为 XxxServlet
// 第三步：指定请求路径 path，注意 / 不能少
// 将来客户端发送请求 http://ip:port/applicationContext/path 就能找到这个 servlet
@WebServlet("/basicUsage")
public class _01_BasicUsageServlet extends HttpServlet {
    /**
     * 第四步：监听来自客户端的请求并返回响应
     * 调用的方法：如果是 get 请求那就调用 doGet 方法，如果是 post 请求那就调用 doPost 方法
     *
     * @param req 请求对象，用来读取客户端发过来的请求数据。包含了客户端请求服务器时的所有信息，如请求方法、请求路径、请求头、请求体等
     * @param resp 响应对象，用来写入响应数据给客户端。包含了服务器要返回给客户端的所有信息，如响应状态码、响应头、响应体等
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("登录成功");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("登录成功");
    }
}

// 第五步：点击 Run 或 Debug 启动 Tomcat，Tomcat 就会把我们的 JavaWeb 项目给自动部署好
// 打开 Postman，访问 http://localhost:9999/helloServlet/basicUsage 就可以了
// 注意：每次修改了服务器的代码后，都需要重新部署甚至重启服务器，否则服务器不会自动更新
