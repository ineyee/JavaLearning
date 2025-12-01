package com.ineyee._04_aop.tx.service;

public interface MoneyService {
    // 转账
    void transfer(Integer fromUserId, Integer toUserId, Double money);
}
