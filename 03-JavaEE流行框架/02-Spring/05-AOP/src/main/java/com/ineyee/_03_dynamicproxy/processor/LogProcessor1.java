package com.ineyee._03_dynamicproxy.processor;

import com.ineyee._03_dynamicproxy.service.UserService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// 自定义一个【日志存储】的 Processor 类，继承 BeanPostProcessor，实现其中的两个方法
// 注意：
//     1、这个类和这两个方法不是针对某一个 bean 对象的，所有 bean 对象都会触发，所以我们可以在这个类里写一些所有 bean 对象的公共处理逻辑
//     2、需要在 Spring 配置文件里注册一下这个类才能生效
public class LogProcessor1 implements BeanPostProcessor {
    // bean 对象的“初始化完成方法”之前会触发
    @Override
    public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 这里的返回值会传递给 postProcessAfterInitialization 方法的参数 bean
        return bean;
    }

    // bean 对象的“初始化完成方法”之后会触发
    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 目前这个日志存储的附加代码，只是给 UserService 业务层附加的，所以得判断一下
        if (bean instanceof UserService || beanName.endsWith("UserService")) {
            // 继承法，CGLib 三方库的动态代理方案、Spring 框架已经默认集成了这个库
            //
            // 使用 rg.springframework.cglib.proxy.Enhancer 类创建一个 enhancer，然后用 enhancer 创建一个业务层代理对象
            // ClassLoader loader：类加载器，类加载器用来将 .class 字节码文件加载到 JVM 里，并解析字节码文件然后生成相应类的类对象
            // Class superclass：业务层，把业务层这个类传进去，将作为业务层代理类的父类
            // setCallback callback：业务层代理的逻辑代码，传一个方法进去，会包含附加代码 + 调用业务层的代码
            //
            // 随便调用哪个类的 getClassLoader 方法都能获取到类加载器
            // 当前 bean 其实就是 userService————我们的业务层对象，我们是要为它动态创建代理对象，所以要把它的类传进去
            // 传一个 lambda 表达式进去，因为业务层代理类继承了业务层类，所以它里面也有 login 等一堆方法，当我们用的业务层代理对象调用 login 等方法时就会触发这个回调
            Enhancer enhancer = new Enhancer();
            enhancer.setClassLoader(getClass().getClassLoader());
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback((MethodInterceptor) (Object proxy, Method method, Object[] args, MethodProxy methodProxy) -> {
                // proxy：业务层代理对象
                // method：业务层的原方法
                // args：业务层原方法的参数

                // 业务层代理调用业务层的代码（相当于中介跟房东对接）
                // bean：当前 bean 其实就是 userService————我们的业务层对象
                // method：业务层的原方法
                // args：业务层原方法的参数
                Object result = method.invoke(bean, args);

                // 业务层的附加代码都抽取到业务层代理中来了
                System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

                return result;
            });

            // 这里的返回值已经不是 Spring 框架根据配置文件默认创建出来的业务层对象了，而是经我们“篡改”后的业务层代理对象
            return enhancer.create();
        }

        // 这里的返回值就是 Spring 框架根据配置文件默认创建出来的 bean 对象
        return bean;
    }
}
