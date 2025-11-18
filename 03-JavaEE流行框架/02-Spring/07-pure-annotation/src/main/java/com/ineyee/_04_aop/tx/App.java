package com.ineyee._04_aop.tx;

import com.ineyee._04_aop.tx.cfg.AppConfig;
import com.ineyee._04_aop.tx.service.MoneyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // 通过 Spring 配置类来创建 applicationContext
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // 通过 beanId 来获取对象
        MoneyService moneyService = (MoneyService) applicationContext.getBean("moneyService");
        moneyService.transfer(1, 2, 1000.0);
    }
}