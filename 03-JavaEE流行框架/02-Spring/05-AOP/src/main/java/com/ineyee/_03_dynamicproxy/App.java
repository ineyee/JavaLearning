package com.ineyee._03_dynamicproxy;

import com.ineyee._03_dynamicproxy.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_03_dynamicproxy.xml");

        // 通过 bean 对象的 id 获取对象
        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");
        Boolean result = userServlet.login("admin", "123456");
        if (result) {
            System.out.println("登录成功");
        } else {
            System.out.println("登录失败");
        }
    }
}