package com.ineyee._05_aoptx;

import com.ineyee._05_aoptx.service.MoneyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_05_aoptx.xml");

        // 通过 bean 对象的 id 获取对象
        MoneyService moneyService = (MoneyService) applicationContext.getBean("moneyService");
        moneyService.transfer(1, 2, 1000.0);
    }
}