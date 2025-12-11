package com.ineyee;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 常见的响应头详见这篇文章：https://www.jianshu.com/p/27a32885fa7a

@Controller
public class TestController {
    // 用 ResponseEntity 返回响应头
    // 需要把返回值搞成 ResponseEntity<T>，T 就是响应体的数据类型
    @GetMapping(value = "/testJson1", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Void> testJson1() {
        // ResponseEntity 必须得有一个响应码，如果我们不关心响应码，可以总是使用成功的 200
        // 这里只返回响应码 + 响应头，没有响应体
        return ResponseEntity
                .ok()
                .header("token", "qwertyuiop")
                .header("app", "nearify")
                .build();
    }
}
