package com.ineyee._02_dibasicuse.servlet;

import com.ineyee._02_dibasicuse.service.UserService;

public class UserServlet {
    // 持有 userService 但无需我们手动创建，由 Spring 自动创建
    private UserService userService;

    // 给成员变量提供 setter 方法
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void remove(Integer id) {
        System.out.println("===>UserServlet remove id：" + id);
        userService.remove(id);
    }
}
