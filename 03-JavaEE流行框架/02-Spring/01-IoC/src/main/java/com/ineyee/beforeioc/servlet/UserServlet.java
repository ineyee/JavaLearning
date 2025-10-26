package com.ineyee.beforeioc.servlet;

import com.ineyee.beforeioc.service.UserService;
import com.ineyee.beforeioc.service.UserServiceImpl;

public class UserServlet {
    // 需要我们手动创建来持有 userService
    private final UserService userService = new UserServiceImpl();

    public void remove(Integer id) {
        System.out.println("===>UserServlet remove id：" + id);
        userService.remove(id);
    }
}
