package com.ineyee._01_beforedi.service;

import com.ineyee._01_beforedi.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceImpl implements UserService {
    // 持有 userDao 但无需我们手动创建，由 Spring 自动创建
    private UserDao userDao;

    @Override
    public Boolean remove(Integer id) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_01_beforedi.xml");
        // 通过 bean 对象的 id 获取对象
        userDao = (UserDao) applicationContext.getBean("userDao");

        System.out.println("===>UserServiceImpl remove id：" + id);
        return userDao.remove(id) > 0;
    }
}
