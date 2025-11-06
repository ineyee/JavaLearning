package com.ineyee._03_dibysetter;

import com.ineyee._03_dibysetter.domain.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_03_dibysetter.xml");

        // 通过 bean 对象的 id 获取对象
        Person person1 = (Person) applicationContext.getBean("person1");
        System.out.println(person1);

        Person person2 = (Person) applicationContext.getBean("person2");
        System.out.println(person2);

        Person person3 = (Person) applicationContext.getBean("person3");
        System.out.println(person3);

        Person person4 = (Person) applicationContext.getBean("person4");
        System.out.println(person4);
    }
}
