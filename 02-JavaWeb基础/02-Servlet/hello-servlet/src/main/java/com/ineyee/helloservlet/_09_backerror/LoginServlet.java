/**
 * 这个 Servlet 演示服务端给客户端返回错误的方案
 */

package com.ineyee.helloservlet._09_backerror;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 需要首先知道的是，servlet 会自动处理 http 协议的标准错误，比如 404、500 等
// 也就是说我们服务端开发者不需要关心万一客户端请求了一个服务器上根本不存在的路径时咋办，因为框架会自动返回 404 错误给客户端，我们不需要编写任何代码
// 因此这就意味着我们服务端开发者只需要关心业务逻辑的错误，比如 token 校验出错、用户名或密码出错等，这些业务逻辑的错误需要我们手动编写代码返回错误信息给客户端
@WebServlet("/login09")
public class LoginServlet extends HttpServlet {
    // 方案一：http 响应状态码和业务状态码平级使用，以前用的多，比如
    // http 响应状态码：
    //     200 代表请求成功
    //     404 代表请求路径出错
    //     500 代表服务器内部出错
    // 业务状态码：
    //     900 代表 token 校验出错
    //     901 代表用户名或密码出错
    //
    // 注意：http 协议规定 Status code must be greater than 99 and less than 1000
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        if (!token.equals("qwertyuiop")) {
            resp.setStatus(900);
            resp.getWriter().write("token 校验出错");
        } else {
            resp.setStatus(200);
            resp.getWriter().write("登录成功");
        }
    }
}
