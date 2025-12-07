package com.ineyee;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 常见的响应状态码详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

@Controller
public class TestController {
    // 使用 @ResponseStatus 注解设置响应码
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String test() {
        return "login success";
    }
}
