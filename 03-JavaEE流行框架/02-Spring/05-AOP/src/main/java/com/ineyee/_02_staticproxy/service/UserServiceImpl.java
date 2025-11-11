package com.ineyee._02_staticproxy.service;

public class UserServiceImpl implements UserService {
    @Override
    public Boolean login(String username, String password) {
        // 业务层只写业务代码
        System.out.println("假设这里是【业务规则校验】的业务代码，去数据库里查询用户和密码是否匹配");
        System.out.println("假设这里是【调用数据层 API】的业务代码，调用数据层的 login 方法");

        return true;
    }
}
