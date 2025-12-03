package com.ineyee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// 登录模块的接口
@Controller
public class LoginController {
    // 登录接口
    // 请求路径为 "/login"，请求方法为 POST
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login() {
        return "login success";
    }

    // 退出登录接口
    // 请求路径为 "/login"，请求方法为 POST
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout() {
        return "logout success";
    }
}
