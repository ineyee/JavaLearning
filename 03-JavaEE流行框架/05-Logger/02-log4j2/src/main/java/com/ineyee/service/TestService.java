package com.ineyee.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

// 并且如果我们的项目里使用了 lombok
// 那么只要用 @Log4j2 注解修饰一下类，lombok 就会自动创建一个变量名为 log 的静态 logger 对象
// 这样一来我们就不用在每个类里手动创建 logger 对象了，直接输出日志即可
@Log4j2
@Service
public class TestService {
    public void test() {
        log.debug("我是调试_DEBUG");
        log.info("我是信息_INFO");
        log.warn("我是警告_WARN");
        log.error("我是错误_ERROR");
    }
}
