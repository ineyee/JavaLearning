package com.ineyee._02_DI;

import com.ineyee._02_DI.cfg.AppConfig;
import com.ineyee._02_DI.domain.Person;
import com.ineyee._02_DI.domain.ThirdPartyClass;
import com.ineyee._02_DI.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // 通过 Spring 配置类来创建 applicationContext
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // 通过 beanId 来获取对象
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");

        ThirdPartyClass thirdPartyClass = (ThirdPartyClass) applicationContext.getBean("thirdPartyClass");
        System.out.println(thirdPartyClass);
    }
}
