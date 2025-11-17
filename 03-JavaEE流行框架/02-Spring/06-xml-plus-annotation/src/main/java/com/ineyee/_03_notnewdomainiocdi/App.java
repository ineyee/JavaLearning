package com.ineyee._03_notnewdomainiocdi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;

public class App {
    public static void main(String[] args) throws Exception {
        // 通过 Spring 配置文件来创建 applicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_03_notnewdomainiocdi.xml");

        // 通过 beanId 来获取对象
        Connection connection = (Connection) applicationContext.getBean("connection");
        System.out.println(connection);
    }
}
