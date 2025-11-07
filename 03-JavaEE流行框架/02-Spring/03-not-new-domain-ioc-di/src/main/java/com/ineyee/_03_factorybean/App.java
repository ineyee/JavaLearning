package com.ineyee._03_factorybean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;

public class App {
    public static void main(String[] args) throws Exception {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_03_factorybean.xml");

        // 通过 bean 对象的 id 获取对象
        Connection connection = (Connection) applicationContext.getBean("connection");
        System.out.println(connection);
    }
}
