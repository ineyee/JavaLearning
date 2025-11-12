package com.ineyee.springbeanlifecycle.processor;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

// 自定义一个类，继承 BeanPostProcessor，实现其中的两个方法
// 注意：
//     1、这个类和这两个方法不是针对某一个 bean 对象的，所有 bean 对象都会触发，所以我们可以在这个类里写一些所有 bean 对象的公共处理逻辑
//     2、需要在 Spring 配置文件里注册一下这个类才能生效
public class BeanProcessor implements BeanPostProcessor {
    // bean 对象的“初始化完成方法”之前会触发
    @Override
    public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("05 - BeanProcessor：BeforeInitialization(Object, String) = " + bean + ", " + beanName);
        // 这里的返回值会传递给 postProcessAfterInitialization 方法的参数 bean
        return bean;
    }

    // bean 对象的“初始化完成方法”之后触发
    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("07 - BeanProcessor：AfterInitialization(Object, String) = " + bean + ", " + beanName);
        // 这里的返回值就是将来通过 Spring 框架最终创建出来的 bean 对象
        return bean;
    }
}
