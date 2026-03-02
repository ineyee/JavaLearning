package _04_登录凭证_token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.PublicKey;

// 在 Postman 里访问 http://localhost:9999/helloCookieSessionToken/pay04 来验证
@WebServlet("/pay04")
public class PayServlet extends HttpServlet {
    private static final PublicKey publicKey;

    static {
        try {
            publicKey = KeyLoadUtil.loadPublicKey("keys/public_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取 token
        String token = req.getHeader("token");

        try {
            // 验证 token
            //   verifyWith：公钥
            //   parse：token
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Integer userId = Integer.valueOf(claims.get("userId").toString());
            String email = claims.get("email").toString();

            // token 中有 userId、email，验证成功代表客户端登录过且 token 还没过期
            // 可以拿这个 userId、email 的值去获取更多的数据返回给客户端
            resp.getWriter().write("登录凭证[" + userId + " " + email + "]有效，通过账户余额支付成功");
        } catch (Exception e) {
            // 验证失败，代表客户端从来没登录过或登录过但 token 过期了
            resp.getWriter().write("登录凭证无效，请先登录");
        }
    }
}
