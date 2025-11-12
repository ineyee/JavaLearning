package com.ineyee._04_aop;

import com.ineyee._04_aop.servlet.OrderServlet;
import com.ineyee._04_aop.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_04_aop.xml");

        // 通过 bean 对象的 id 获取对象
        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");
        Boolean result = userServlet.login("admin", "123456");
        if (result) {
            System.out.println("登录成功");
        } else {
            System.out.println("登录失败");
        }

        // 通过 bean 对象的 id 获取对象
        OrderServlet orderServlet = (OrderServlet) applicationContext.getBean("orderServlet");
        orderServlet.view();
        Boolean payResult = orderServlet.pay();
        if (payResult) {
            System.out.println("支付成功");
        } else {
            System.out.println("支付失败");
        }
    }
}