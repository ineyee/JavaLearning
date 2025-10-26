package com.ineyee.ioc.servlet;

import com.ineyee.ioc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServlet {
    // 持有 userService 但无需我们手动创建，由 Spring 自动创建
    private UserService userService;

    public void remove(Integer id) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 通过 bean 对象的 id 获取对象
        userService = (UserService) applicationContext.getBean("userService");

        System.out.println("===>UserServlet remove id：" + id);
        userService.remove(id);
    }
}
