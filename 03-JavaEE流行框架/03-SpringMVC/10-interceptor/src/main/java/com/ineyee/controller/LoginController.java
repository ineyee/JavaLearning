package com.ineyee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @PostMapping("/login")
    @ResponseBody
    public String login() {
        return "login success";
    }
}
