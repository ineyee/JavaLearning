package com.ineyee.common.filter;

import com.ineyee.common.api.error.UserServiceError;
import com.ineyee.common.context.TokenInfo;
import com.ineyee.common.context.UserContext;
import com.ineyee.common.util.KeyLoadUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final PublicKey publicKey;

    static {
        try {
            publicKey = KeyLoadUtil.loadPublicKey("keys/public_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 白名单路径
    private static final List<String> WHITE_LIST = List.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/user/login"
    );

    // 设置白名单路径无需过滤
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return WHITE_LIST.stream().anyMatch(path::contains);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                // 注意 filter 里不能通过抛业务异常的方式给客户端返回响应
                // 因为全局异常处理仅仅针对是的 controller 里抛出来的异常，而这里 filter 里出错了还没到达 controller 那一步
                // 所以只能通过 response 给客户端返回响应
                backError(response);
                return;
            }

            String token = header.substring(7);

            // 验证 token
            //   verifyWith：公钥
            //   parse：token
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 把 token 里携带的业务信息存进全局 UserContext，方便后续各个 Controller 或 Service 获取
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setEmail(claims.get("email").toString());
            UserContext.setTokenInfo(tokenInfo);

            // 让请求进入 Controller
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 验证失败，代表客户端未登录或登录过但 token 过期了
            backError(response);
            return;
        } finally {
            // 必须 clear，否则线程复用会污染数据
            UserContext.clearTokenInfo();
        }
    }

    private void backError(HttpServletResponse response) throws IOException {
        Integer code = UserServiceError.INVALID_TOKEN.getCode();
        String message = UserServiceError.INVALID_TOKEN.getMessage();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
                {
                    "code": %d,
                    "message": "%s"
                }
                """.formatted(code, message));
    }
}
