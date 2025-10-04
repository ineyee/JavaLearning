package _03_登录凭证_session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// 在浏览器里访问 http://localhost:9999/helloCookieSessionToken/pay03 来验证
@WebServlet("/pay03")
public class PayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 客户端会通过请求头里的“Cookie”字段把“JSESSIONID=某个 session 会话对象的 id”传递给服务端
        // 获取服务器上对应的 session 会话对象（false 代表仅获取，如果没有也不会创建新的），如果客户端没有携带“Cookie: JSESSIONID=某个 session 会话对象的 id”，那么肯定返回 null
        HttpSession session = req.getSession(false);

        // 验证 session
        if (session == null) {
            // session 为 null，代表客户端从来没登录过或登录过但 session 过期了
            resp.getWriter().write("登录凭证无效，请先登录");
        } else {
            String userId = (String) session.getAttribute("userId");
            if (userId == null) {
                // session 中没有 userId，代表客户端从来没登录过或登录过但 session 过期了
                resp.getWriter().write("登录凭证无效，请先登录");
            } else {
                // session 中有 userId，代表客户端登录过且 session 还没过期
                // 可以拿这个 userId 的值去获取更多的数据返回给客户端
                resp.getWriter().write("登录凭证[" + Integer.valueOf(userId) + "]有效，通过账户余额支付成功");
            }
        }
    }
}
