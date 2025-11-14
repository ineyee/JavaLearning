package com.ineyee._05_aoptx.service;

public interface MoneyService {
    // 转账
    void transfer(Integer fromUserId, Integer toUserId, Double money);
}
