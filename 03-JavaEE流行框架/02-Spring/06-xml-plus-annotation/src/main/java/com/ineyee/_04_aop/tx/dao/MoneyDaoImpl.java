package com.ineyee._04_aop.tx.dao;

import org.springframework.stereotype.Repository;

@Repository
public class MoneyDaoImpl implements MoneyDao {
    @Override
    public void update(Integer userId, Double money) {
        System.out.println("【假设这里在调用数据库的 API】，为 " + userId + " 存取钱 " + money);
    }
}
