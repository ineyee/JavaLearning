package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.util.KeyLoadUtil;
import com.ineyee.pojo.dto.LoginDto;
import com.ineyee.pojo.req.LoginReq;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

// 这里就不演示完整的用户模块了
// 只演示生成并返回 token + 验证 token，如果要做缓存 token 的话、也有演示

// 1、实际开发中哪些接口需要返回 token？
// 登录接口：必须返回
// 三方登录接口：必须返回
// 注册接口：如果我们要做成注册成功后自动登录、那就必须返回，否则可以只返回注册成功的状态、让用户再走一遍登录流程

// 2、实际开发中哪些接口需要携带 token？
// 登录接口、三方登录接口、注册接口、获取验证码接口、忘记密码修改密码接口、SwaggerUI 相关的两个接口不需要携带 token
// 其它业务接口都需要携带 token、验证 token，我们可以自定义一个 filter 来统一拦截验证 token，而不是每个接口都单独验证 token

// 3、实际开发中登录接口成功后，除了返回 token、要返回用户信息吗？
// 方案一：不返回用户信息，优点是单接口单职责，缺点是客户端需要多一次请求来获取用户信息、可能增加一次网络延迟
// 方案二（推荐）：返回用户信息，缺点是接口不再单职责，优点是减少请求次数、提升用户体验（如客户端可以立即展示用户名和用户头像，可以根据角色/权限信息动态决定展示哪些菜单等）

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户模块")
public class UserController {
    private static final PrivateKey privateKey;

    // 演示缓存字符串类型的数据
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 演示缓存 Hash 类型的数据
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    static {
        try {
            privateKey = KeyLoadUtil.loadPrivateKey("keys/private_key.pem");
        } catch (Exception e) {
            throw new RuntimeException("加载私钥失败", e);
        }
    }

    @PostMapping("login")
    @Operation(summary = "登录")
    @SecurityRequirements()  // 覆盖全局配置，此接口不需要 token
    public HttpResult<LoginDto> login(@Valid @RequestBody LoginReq req) {
        // 先根据邮箱去数据库里查询用户是否存在
        //     用户不存在的话，直接给客户端响应“用户不存在”
        //     用户存在的话，再比对一下传过来的密码跟数据库里的一样不一样
        //         不一样的话，直接给客户端响应“账号或密码错误”
        //         一样的话，判定为登录接口走成功

        // 假设这里服务端判断到登录接口走成功...
        //
        // 服务端用非对称加密里的私钥生成 token
        //   claims：携带的业务信息，将来其它接口验证 token 时可以通过业务信息决定是哪个用户，token 里一般会携带用户的唯一标识、用户名或邮箱、角色或权限等业务信息
        //   expiration：过期时间（单位秒）
        //   signWith：私钥
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", req.getEmail());
        String token = Jwts.builder()
                .claims(claims)
                .expiration(Date.from(Instant.now().plusSeconds(30 * 24 * 60 * 60)))
                .signWith(privateKey)
                .compact();

        // 服务端给客户端返回 token
        LoginDto dto = new LoginDto();
        // token
        dto.setToken(token);
        // 用户信息
        dto.setEmail(req.getEmail());
        return HttpResult.ok(dto);
    }

    @PostMapping("loginWithRedis")
    @Operation(summary = "登录")
    @SecurityRequirements()  // 覆盖全局配置，此接口不需要 token
    public HttpResult<LoginDto> loginWithRedis(@Valid @RequestBody LoginReq req) {
        // 先根据邮箱去数据库里查询用户是否存在
        //     用户不存在的话，直接给客户端响应“用户不存在”
        //     用户存在的话，再比对一下传过来的密码跟数据库里的一样不一样
        //         不一样的话，直接给客户端响应“账号或密码错误”
        //         一样的话，判定为登录接口走成功

        // 假设这里服务端判断到登录接口走成功...
        // 生成 token
        String token = UUID.randomUUID().toString();
        // 把 token 缓存到 redis 中
        // 类似这样的结构 { "user:email:qwertyuiop@qq.com": 1234567890 }
        ValueOperations<String, String> stringOps = stringRedisTemplate.opsForValue();
        stringOps.set("user:email:" + req.getEmail(), token, 30, TimeUnit.DAYS);
        // 把关键用户信息缓存到 redis 中
        // 类似这样的结构 {
        //                "user:token:1234567890":{
        //                  "email":"qwertyuiop@qq.com"
        //                }
        //              }
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        hashOps.put("user:token:" + token, "email", req.getEmail());
        redisTemplate.expire("user:token:" + token, 30, TimeUnit.DAYS);

        // 服务端给客户端返回 token
        LoginDto dto = new LoginDto();
        // token
        dto.setToken(token);
        // 用户信息
        dto.setEmail(req.getEmail());
        return HttpResult.ok(dto);
    }

    @PostMapping("logout")
    @Operation(summary = "退出登录")
    public HttpResult<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        // 先从 Redis 里查出 token 对应的用户信息，拿到 email
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        Object email = hashOps.get("user:token:" + token, "email");

        // 删除 user:token:{token}
        redisTemplate.delete("user:token:" + token);

        // 删除 user:email:{email}
        if (email != null) {
            stringRedisTemplate.delete("user:email:" + email);
        }

        return HttpResult.ok();
    }
}
