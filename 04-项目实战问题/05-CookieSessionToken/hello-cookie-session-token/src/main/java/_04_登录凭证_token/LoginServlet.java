package _04_登录凭证_token;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 在 Postman 里访问 http://localhost:9999/helloCookieSessionToken/login04 来验证
@WebServlet("/login04")
public class LoginServlet extends HttpServlet {
    private static final PrivateKey privateKey;

    static {
        try {
            privateKey = KeyLoadUtil.loadPrivateKey("keys/private_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 假设这里服务端判断到登录接口走成功...

        // 服务端生成 token
        //   claims：携带的业务信息，将来其它接口验证 token 时可以通过业务信息决定是哪个用户
        //   expiration：过期时间（单位秒）
        //   signWith：私钥
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "123456");
        claims.put("email", "zhangsan@qq.com");
        String token = Jwts.builder()
                .claims(claims)
                .expiration(Date.from(Instant.now().plusSeconds(30 * 24 * 60 * 60)))
                .signWith(privateKey)
                .compact();

        // 服务端给客户端返回 token
        resp.getWriter().write("登录成功，token = " + token);
    }
}
