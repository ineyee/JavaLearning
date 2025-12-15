package com.ineyee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 常见的响应状态码详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

@Controller
public class TestController {
    // 用 ResponseEntity 返回响应码
    // 需要把返回值搞成 ResponseEntity<T>，T 就是响应体的数据类型
    @GetMapping(path = "/test")
    @ResponseBody
    public ResponseEntity<Void> test() {
        // 这里只返回响应码，没有响应头和响应体
        return ResponseEntity.status(500).build();
    }
}
