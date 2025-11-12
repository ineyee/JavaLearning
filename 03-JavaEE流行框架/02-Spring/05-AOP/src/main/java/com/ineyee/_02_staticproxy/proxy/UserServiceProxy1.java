package com.ineyee._02_staticproxy.proxy;

import com.ineyee._02_staticproxy.service.UserService;
import com.ineyee._02_staticproxy.service.UserServiceImpl;

// 继承法
public class UserServiceProxy1 extends UserServiceImpl {
    // 业务层代理持有业务层
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Boolean login(String username, String password) {
        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
        Boolean result = userService.login(username, password);

        // 业务层的附加代码都抽取到业务层代理中来了
        System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

        return result;
    }
}
