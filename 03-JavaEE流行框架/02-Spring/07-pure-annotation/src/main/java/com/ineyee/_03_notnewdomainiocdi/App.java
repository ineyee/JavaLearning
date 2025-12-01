package com.ineyee._03_notnewdomainiocdi;

import com.ineyee._03_notnewdomainiocdi.cfg.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;

public class App {
    public static void main(String[] args) throws Exception {
        // 通过 Spring 配置类来创建 applicationContext
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // 通过 beanId 来获取对象
        Connection connection = (Connection) applicationContext.getBean("connection");
        System.out.println(connection);
    }
}
