package com.ineyee._02_dibasicuse.dao;

public class UserDaoImpl implements UserDao {
    @Override
    public int remove(Integer id) {
        System.out.println("===>UserDaoImpl remove id：" + id);
        return 0;
    }
}
