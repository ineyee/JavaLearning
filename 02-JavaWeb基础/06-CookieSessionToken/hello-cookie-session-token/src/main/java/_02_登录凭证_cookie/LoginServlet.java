package _02_登录凭证_cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 在浏览器里访问 http://localhost:9999/helloCookieSessionToken/login02 来验证
@WebServlet("/login02")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 假设这里服务端判断到登录接口走成功...

        // 服务端给客户端设置 cookie
        //   * cookie 名（String）：这里我们设置为登录成功后拿到的 userId
        //   * cookie 值（String）：这里我们设置为 userId 的值
        //   * cookie 过期时间（单位秒）
        Cookie cookie = new Cookie("userId", "123456");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        // cookie 会通过响应头里的“Set-Cookie”字段传递给客户端
        resp.addCookie(cookie);

        // 服务端给客户端响应数据
        resp.getWriter().write("登录成功");
    }
}
