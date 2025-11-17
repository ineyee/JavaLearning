package com.ineyee._02_DI.servlet;

import com.ineyee._02_DI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class UserServlet {
    private UserService userService;

    @Autowired
    @Qualifier("userService1")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
