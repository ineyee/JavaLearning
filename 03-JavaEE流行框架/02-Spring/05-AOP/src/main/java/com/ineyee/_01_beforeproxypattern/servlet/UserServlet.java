package com.ineyee._01_beforeproxypattern.servlet;

import com.ineyee._01_beforeproxypattern.service.UserService;

public class UserServlet {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Boolean login(String username, String password) {
        // 控制器层直接调用业务层的代码
        return userService.login(username, password);
    }
}