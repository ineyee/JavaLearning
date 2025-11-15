package com.ineyee._05_aoptx.service;

import com.ineyee._05_aoptx.dao.MoneyDao;

public class MoneyServiceImpl implements MoneyService {
    MoneyDao moneyDao;

    public void setMoneyDao(MoneyDao moneyDao) {
        this.moneyDao = moneyDao;
    }

    @Override
    public void transfer(Integer fromUserId, Integer toUserId, Double money) {
        // 这里会自动切入开启事务的代码
        // fromUserId 扣钱
        moneyDao.update(fromUserId, -money);

        // 模拟业务执行过程中出现异常
        System.out.println(10 / 0);

        // toUserId 加钱
        moneyDao.update(toUserId, money);

        // 成功后，这里会自动切入会提交事务的代码
        // 失败后，这里会自动切入会回滚事务的代码
    }
}
