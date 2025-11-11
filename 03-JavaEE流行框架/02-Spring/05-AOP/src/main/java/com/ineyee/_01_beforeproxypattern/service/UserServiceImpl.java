package com.ineyee._01_beforeproxypattern.service;

public class UserServiceImpl implements UserService {
    @Override
    public Boolean login(String username, String password) {
        // 业务层既写业务代码
        System.out.println("假设这里是【业务规则校验】的业务代码，去数据库里查询用户和密码是否匹配");
        System.out.println("假设这里是【调用数据层 API】的业务代码，调用数据层的 login 方法");

        // 业务层还写附加代码
        System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

        return true;
    }
}
