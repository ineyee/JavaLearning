package com.ineyee._02_staticproxy;

import com.ineyee._02_staticproxy.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_02_staticproxy.xml");

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