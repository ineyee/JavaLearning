package com.ineyee.beforeioc.dao;

public class UserDaoImpl implements UserDao {
    @Override
    public int remove(Integer id) {
        System.out.println("===>UserDaoImpl remove id：" + id);
        return 0;
    }
}
