package com.ineyee._02_DI;

import com.ineyee._02_DI.domain.Person;
import com.ineyee._02_DI.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 通过 Spring 配置文件来创建 applicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_02_DI.xml");

        // 通过 beanId 来获取对象
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");
    }
}
