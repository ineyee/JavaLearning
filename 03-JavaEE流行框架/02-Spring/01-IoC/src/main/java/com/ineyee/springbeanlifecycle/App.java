package com.ineyee.springbeanlifecycle;

import com.ineyee.springbeanlifecycle.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_springbeanlifecycle.xml");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_springbeanlifecycle.xml");

        // 通过 bean 对象的 id 获取对象
        UserService userService = (UserService) applicationContext.getBean("userService");
        Boolean result = userService.login("admin", "123456");
        if (result) {
            System.out.println("登录成功");
        } else {
            System.out.println("登录失败");
        }

        // 为了演示销毁方法，这里我们关闭 applicationContext，这样一来 IoC 容器中的对象都会销毁
        // 并且这个 close() 方法只有 ClassPathXmlApplicationContext 子类才有，所以我们不用 ApplicationContext 父类指向子类对象了，直接用子类指向
        applicationContext.close();
    }
}