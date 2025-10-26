package com.ineyee.beforeioc.service;

import com.ineyee.beforeioc.dao.UserDao;
import com.ineyee.beforeioc.dao.UserDaoImpl;

public class UserServiceImpl implements UserService {
    // 需要我们手动创建来持有 userDao
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Boolean remove(Integer id) {
        System.out.println("===>UserServiceImpl remove id：" + id);
        return userDao.remove(id) > 0;
    }
}
