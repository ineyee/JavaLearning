package com.ineyee._01_IoC;

import com.ineyee._01_IoC.dao.UserDao;
import com.ineyee._01_IoC.domain.User;
import com.ineyee._01_IoC.service.UserService;
import com.ineyee._01_IoC.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 通过 Spring 配置文件来创建 applicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_01_IoC.xml");

        // 通过 beanId 来获取对象
        User user = (User) applicationContext.getBean("user");
        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserDao userDao = (UserDao) applicationContext.getBean("userDao");
    }
}