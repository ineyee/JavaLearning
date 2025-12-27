package com.ineyee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 创建 SpringBoot 项目的入口类，我们一般把它命名为 Application
// 用 @SpringBootApplication 注解修饰一下这个类，来标识它是 SpringBoot 项目的入口类
// 并且 @SpringBootApplication 注解还包含了 @ComponentScan 注解的功能，它默认就会扫描当前类所在包及其子包下所有的类，创建对象并放到 IoC 容器中
@SpringBootApplication
public class Application {
    // 为项目的入口类添加 main 方法，作为项目的入口方法
    public static void main(String[] args) {
        // 固定写法，启动 SpringBoot 应用
        SpringApplication.run(Application.class, args);
    }
}
