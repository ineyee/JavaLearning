package com.ineyee._05_diconverter;

import com.ineyee._05_diconverter.domain.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_05_diconverter.xml");

        // 通过 bean 对象的 id 获取对象
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }
}
