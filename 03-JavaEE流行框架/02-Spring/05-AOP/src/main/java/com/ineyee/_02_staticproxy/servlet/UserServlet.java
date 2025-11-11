package com.ineyee._02_staticproxy.servlet;

import com.ineyee._02_staticproxy.service.UserService;

public class UserServlet {
    // 控制器层表面还是持有业务层，实际却是持有业务层代理（本质原因是修改了注入的对象）
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Boolean login(String username, String password) {
        // 控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码（相当于租客跟中介对接）
        return userService.login(username, password);
    }
}