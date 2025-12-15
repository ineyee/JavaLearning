package com.ineyee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

// 需要首先知道的是，servlet 会自动处理 http 协议的标准错误，比如 404、500 等
// 也就是说我们服务端开发者不需要关心万一客户端请求了一个服务器上根本不存在的路径时咋办，因为框架会自动返回 404 错误给客户端，我们不需要编写任何代码
// 因此这就意味着我们服务端开发者只需要关心 http 请求成功（200）时的业务逻辑的错误，比如 token 校验出错、用户名或密码出错等，这些业务逻辑的错误需要我们手动编写代码返回错误信息给客户端
@Controller
public class LoginController {
    // 方案一：http 响应状态码和业务状态码平级使用，以前用的多，比如
    // http 响应状态码：
    //     200 代表请求成功
    //     404 代表请求路径出错
    //     500 代表服务器内部出错
    // 业务状态码：
    //     900 代表 token 校验出错
    //     901 代表用户名或密码出错
    //
    // 注意：http 协议规定 Status code must be greater than 99 and less than 1000
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestHeader(value = "token", required = false) String token) {
        if (token == null || !token.equals("qwertyuiop")) {
            return ResponseEntity.status(900).body("token 校验出错");
        } else {
            return ResponseEntity.status(200).body("登录成功");
        }
    }

    // 方案二：http 响应状态码和业务状态码分层使用，现在用的多，比如
    // http 响应状态码：
    //     200 代表请求成功
    //     404 代表请求路径出错
    //     500 代表服务器内部出错
    // 业务状态码：
    //     0 代表没有业务逻辑错误
    //     -1001 代表 token 校验出错
    //     -1002 代表用户名或密码出错
    //
    // 这种方式的优势在于：
    // 1、业务状态码跟 http 标准响应状态码不再平级、而是下级，所以就没有了 99~1000 的限制，可以根据业务量需要随便定义多少个状态码，也不至于跟 http 标准响应状态码冲突
    // 2、这种方案理解起来是更合理的，具体地说当服务端返回 404、500 等错误时，是代表 http 请求本身出了问题，根本不会走业务；而当服务器返回业务状态码的错误时，http 请求本身其实还是成功的、会返回 200、这很合理，只是我们的业务逻辑出错了而已；因此业务状态码变成 http 标准响应状态码的下级，是更合理的
    // 3、对于我们服务端开发者来说，这种方案编写起代码来其实是更清晰的，具体地说就是 http 标准响应状态码交由框架自动处理，而我们只需要关心业务逻辑的状态码。而业务逻辑的状态码必然发生在 http 标准响应状态码为 200 的情况下，因为如果发生其它 http 标准响应状态码的话压根不会走业务逻辑、直接就拦截下来了。总之这样一来，我们服务端开发者就可以在写代码的时候“无视” http 标准响应状态码、就好像不存在这么一种东西一样，而是全部专注于业务逻辑状态码的编写
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout(@RequestHeader(value = "token", required = false) String token) {
        if (token == null || !token.equals("qwertyuiop")) {
            return ResponseEntity.ok().body("{\"code\":-1001,\"message\":\"token 校验出错\"}");
        } else {
            return ResponseEntity.ok().body("{\"code\":0,\"message\":\"退出登录成功\"}");
        }
    }
}
