package com.ineyee.scopeproperty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_scopeproperty.xml");

        Person person1 = (Person) applicationContext.getBean("person");
        System.out.println(person1); // Person@449a4f23，hashcode 跟 person2 一样

        Person person2 = (Person) applicationContext.getBean("person");
        System.out.println(person2); // Person@449a4f23，hashcode 跟 person1 一样
    }
}