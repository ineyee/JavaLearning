package _02_登录凭证_cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 在浏览器里访问 http://localhost:9999/helloCookieSessionToken/pay02 来验证
@WebServlet("/pay02")
public class PayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // cookie 会通过请求头里的“Cookie”字段传递给服务端
        // 获取 cookies，如果客户端没有携带 cookie，那么 cookies 为 null
        Cookie[] cookies = req.getCookies();

        // 验证 cookie
        if (cookies == null) {
            // cookies 为 null，代表客户端从来没登录过或登录过但 cookie 过期了
            resp.getWriter().write("登录凭证无效，请先登录");
        } else {
            String userId = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                }
            }

            if (userId == null) {
                // 客户端携带了 cookie，但 cookie 中没有 userId，代表客户端从来没登录过或登录过但 cookie 过期了
                resp.getWriter().write("登录凭证无效，请先登录");
            } else {
                // 客户端携带了 cookie，且 cookie 中有 userId，代表客户端登录过且 cookie 还没过期
                // 可以拿这个 userId 的值去获取更多的数据返回给客户端
                resp.getWriter().write("登录凭证[" + Integer.valueOf(userId) + "]有效，通过账户余额支付成功");
            }
        }
    }
}
