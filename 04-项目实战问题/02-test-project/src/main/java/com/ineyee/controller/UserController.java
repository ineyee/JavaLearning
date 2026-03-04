package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.error.UserServiceError;
import com.ineyee.common.util.KeyLoadUtil;
import com.ineyee.pojo.dto.UserCreateDto;
import com.ineyee.pojo.req.UserCreateReq;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 这里就不演示完整的用户模块了
// 只演示生成并返回 token + 验证 token

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
    public HttpResult<UserCreateDto> login(@Valid @RequestBody UserCreateReq req) {
        // 假设这里服务端判断到登录接口走成功...
        if (req.getEmail().equals("123456") && req.getPassword().equals("123456")) {
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
            UserCreateDto dto = new UserCreateDto();
            // token
            dto.setToken(token);
            // 用户信息
            dto.setEmail(req.getEmail());
            return HttpResult.ok(dto);
        } else {
            return HttpResult.error(UserServiceError.WRONG_EMAIL_PASSWORD.getCode(), UserServiceError.WRONG_EMAIL_PASSWORD.getMessage());
        }
    }
}
