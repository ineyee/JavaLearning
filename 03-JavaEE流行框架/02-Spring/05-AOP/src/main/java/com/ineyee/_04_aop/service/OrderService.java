package com.ineyee._04_aop.service;

public class OrderService {
    public Boolean pay() {
        // 业务层只写业务代码
        System.out.println("假设这里是【调用数据层 API】的业务代码，调用数据层的 pay 方法");

        return false;
    }

    public void view() {
        // 业务层只写业务代码
        System.out.println("假设这里是【调用数据层 API】的业务代码，调用数据层的 view");
    }
}
