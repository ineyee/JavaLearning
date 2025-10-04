package _03_登录凭证_session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// 在浏览器里访问 http://localhost:9999/helloCookieSessionToken/login03 来验证
@WebServlet("/login03")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 假设这里服务端判断到登录接口走成功...

        // 创建针对某个客户端的 session 会话对象（session 会话对象是存储在服务器上 Java 项目内存里的）
        //
        // getSession() 方法内部的原理：
        // 首先检查客户端发过来的请求头里有没有“Cookie: JSESSIONID=某个 session 会话对象的 id”
        //   如果有，代表服务器之前已经跟这个客户端建立过 session 会话对象了，那就直接返回这个 id 对应的 session 会话对象
        //   如果没有，就创建一个新的 session 会话对象来返回，并且在响应本次请求时自动添加响应头“Set-Cookie: JSESSIONID=新 session 会话对象的 id”字段传递给客户端
        HttpSession session = req.getSession();
        // 我们要把什么用户数据存储在 session 会话对象里（也即存储在服务器上 Java 项目内存里），这里我们设置为登录成功后拿到的 userId 及其值
        // session 过期时间（单位秒）
        session.setAttribute("userId", "123456");
        session.setMaxInactiveInterval(30 * 24 * 60 * 60);

        // 服务端给客户端响应数据
        resp.getWriter().write("登录成功");
    }
}
