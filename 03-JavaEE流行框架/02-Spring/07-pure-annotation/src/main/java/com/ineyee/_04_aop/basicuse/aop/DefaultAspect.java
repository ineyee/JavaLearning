package com.ineyee._04_aop.basicuse.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DefaultAspect {
    @Pointcut("execution(* com.ineyee._04_aop.basicuse.service..*.*(..))")
    public void pointcut() {
    }

    // 我们可以认为这个方法就是业务层代理的方法
    // 我们只需要专心考虑附加代码怎么写就可以了
    @Around("pointcut()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
        Object result = point.proceed();

        // 业务层的附加代码都抽取到业务层代理中来了
        System.out.println("假设这里是【日志存储】的附加代码");

        return result;
    }
}
