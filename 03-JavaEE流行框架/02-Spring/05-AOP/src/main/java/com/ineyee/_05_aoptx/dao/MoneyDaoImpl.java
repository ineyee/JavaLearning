package com.ineyee._05_aoptx.dao;

public class MoneyDaoImpl implements MoneyDao {
    @Override
    public void update(Integer userId, Double money) {
        System.out.println("【假设这里在调用数据库的 API】，为 " + userId + " 存取钱 " + money);
    }
}
