package com.ineyee._04_aop.basicuse;

import com.ineyee._04_aop.basicuse.cfg.AppConfig;
import com.ineyee._04_aop.basicuse.servlet.OrderServlet;
import com.ineyee._04_aop.basicuse.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // 通过 Spring 配置类来创建 applicationContext
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // 通过 beanId 来获取对象
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