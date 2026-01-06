package com.ineyee.controller;

import com.ineyee.service.TestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    // LogManager.getLogger 的时候把当前类传进去，就能在日志中看到当前类名
    private static final Logger log =
            LogManager.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        log.debug("我是调试_DEBUG");
        log.info("我是信息_INFO");
        log.warn("我是警告_WARN");
        log.error("我是错误_ERROR");

        testService.test();

        return "test";
    }
}
