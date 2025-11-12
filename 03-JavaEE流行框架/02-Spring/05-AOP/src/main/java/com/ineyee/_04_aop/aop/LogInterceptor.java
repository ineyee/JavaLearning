package com.ineyee._04_aop.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.Nullable;

// 自定义一个【日志存储】的 Interceptor 类，实现 MethodInterceptor 接口，实现其中的一个方法
// 注意：需要在 Spring 配置文件里注册一下这个类才能生效
public class LogInterceptor implements MethodInterceptor {
    // 我们可以认为这个方法就是业务层代理的方法
    // 里面的代码很简单了，不用像我们自己实现动态代理那样考虑各种乱七八糟的参数是什么意思、怎么传
    // 这里只需要简单一调用，参数都会自动处理
    // 我们只需要专心考虑附加代码怎么写就可以了
    @Override
    public @Nullable Object invoke(MethodInvocation invocation) throws Throwable {
        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
        Object result = invocation.proceed();

        // 业务层的附加代码都抽取到业务层代理中来了
        System.out.println("假设这里是【日志存储】的附加代码");

        return result;
    }
}
