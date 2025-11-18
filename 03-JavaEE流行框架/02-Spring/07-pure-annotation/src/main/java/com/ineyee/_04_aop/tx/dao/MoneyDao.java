package com.ineyee._04_aop.tx.dao;

public interface MoneyDao {
    // 存取钱
    void update(Integer userId, Double money);
}
