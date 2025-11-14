package com.ineyee._05_aoptx.service;

import com.ineyee._05_aoptx.dao.MoneyDao;

public class MoneyServiceImpl implements MoneyService {
    MoneyDao moneyDao;

    public void setMoneyDao(MoneyDao moneyDao) {
        this.moneyDao = moneyDao;
    }

    @Override
    public void transfer(Integer fromUserId, Integer toUserId, Double money) {
        // fromUserId 扣钱
        moneyDao.update(fromUserId, -money);
        // toUserId 加钱
        moneyDao.update(toUserId, money);

        System.out.println("转账成功");
    }
}
