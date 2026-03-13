package com.ineyee.common.filter;

import com.ineyee.common.api.error.UserServiceError;
import com.ineyee.common.context.TokenInfo;
import com.ineyee.common.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class TokenFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 白名单路径
    private static final List<String> WHITE_LIST = List.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/user/loginWithRedis"
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
                backError(response);
                return;
            }

            String token = header.substring(7);

            // 我们可以真得去 Redis 缓存中看看有没有这个 token（不存在或已过期，查询结果都将是没有这个 token）
            // xxx.get("user:email:" + email)
            // 存在的话再去 Redis 缓存中获取用户信息
            //
            // 但其实没必要分两步，直接去 Redis 缓存中看看有没有 token 对应的用户信息即可
            // 因为两个缓存是一起存进去的，两者的有效期差不了几微秒
            // 一步顶两步
            HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
            Object email = hashOps.get("user:token:" + token, "email");
            if (email == null) {
                backError(response);
                return;
            }

            // 把用户信息存进全局 UserContext，方便后续各个 Controller 或 Service 获取
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setEmail(email.toString());
            UserContext.setTokenInfo(tokenInfo);

            // 让请求进入 Controller
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            backError(response);
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