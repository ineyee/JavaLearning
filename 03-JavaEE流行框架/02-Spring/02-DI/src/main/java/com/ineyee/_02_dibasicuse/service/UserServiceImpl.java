package com.ineyee._02_dibasicuse.service;

import com.ineyee._02_dibasicuse.dao.UserDao;

public class UserServiceImpl implements UserService {
    // 持有 userDao 但无需我们手动创建，由 Spring 自动创建
    private UserDao userDao;

    // 给成员变量提供 setter 方法
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Boolean remove(Integer id) {
        System.out.println("===>UserServiceImpl remove id：" + id);
        return userDao.remove(id) > 0;
    }
}
