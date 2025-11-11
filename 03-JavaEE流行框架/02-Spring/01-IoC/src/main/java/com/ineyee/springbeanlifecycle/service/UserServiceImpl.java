package com.ineyee.springbeanlifecycle.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class UserServiceImpl implements UserService, BeanNameAware, ApplicationContextAware, InitializingBean, DisposableBean {
    public UserServiceImpl() {
        System.out.println("01 - 构造方法：UserServiceImpl - UserServiceImpl()");
    }

    private Object userDao;

    public void setUserDao(Object userDao) {
        this.userDao = userDao;
        System.out.println("02 - 依赖注入的 setter 方法：UserServiceImpl - setUserDao(Object) = " + userDao);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("03 - BeanNameAware 接口的方法：UserServiceImpl - setBeanName(String) = " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("04 - ApplicationContextAware 接口的方法：UserServiceImpl - setApplicationContext(ApplicationContext) = " + applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("0601 - InitializingBean 接口的方法：UserServiceImpl - afterPropertiesSet()");
    }

    public void onInit() {
        System.out.println("0602 - init-method 初始化完成方法：UserServiceImpl - onInit()");
    }

    @Override
    public Boolean login(String username, String password) {
        System.out.println("08 - 我们自定义的各种业务方法：UserServiceImpl - login(String, String) = " + username + ", " + password);
        return true;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("0901 - DisposableBean 销毁方法：UserServiceImpl - destroy()");
    }

    public void onDestroy() {
        System.out.println("0902 - destroy-method 即将销毁方法：UserServiceImpl - onDestroy()");
    }
}
