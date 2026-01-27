package com.ineyee.common.api.exception;

import com.ineyee.common.api.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 第一步：自定义一个 GlobalExceptionHandler 类
// 用 @ControllerAdvice 注解修饰一下这个类，这个注解有两个用途，一是放到 IoC 容器里，二是通过 AOP 把异常处理代码切入到 controller 层的方法里
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 第二步：自定义一个方法
    // 用 @ExceptionHandler 注解修饰一下这个方法，表明这个方法专门用来处理 ServiceException 业务异常
    // 这里是业务异常 ServiceException，业务异常各有各的错误码和错误信息，用户在客户端看到错误信息是有提示意义的
    @ExceptionHandler(ServiceException.class)
    public HttpResult<Void> handleServiceException(ServiceException e) {
        // 给客户端响应错误
        log.warn("业务异常：{}", e.toString());
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    // 用 @ExceptionHandler 注解修饰一下这个方法，表明这个方法专门用来处理业务异常以外的系统异常
    // 这里是系统异常 Exception，系统异常是没有 code 的、所以我们把系统异常的错误码统一为 -100000，错误信息自动获取、当然客户端不一定会把这个信息展示给用户看、但在联调接口时非常有用
    @ExceptionHandler(Exception.class)
    public HttpResult<Void> handleException(Exception e) {
        // 给客户端响应错误
        log.error("系统异常：{}", e.getMessage());
        return HttpResult.error(e.getMessage());
    }
}

// 总的来说，有了这个全局异常处理，数据层、业务层、控制器层都不需要再写任何一句 try-catch 来捕获系统异常和业务异常，全都由这里兜底处理
//
// 并且也正是因为这个全局异常处理的存在，才使得我们能够按照【方案二：http 响应状态码和业务状态码分层使用】来给客户端返回响应
// 因为所有的异常都被拦截处理了，所以服务器才能在遇到任何异常时总能给客户端正常响应 200，而不会响应 404、500 之类的状态码
//
// 但是如果我们就是希望按照【方案一：http 响应状态码和业务状态码平级使用】来给客户端返回响应
// 那就去掉系统异常拦截，这样一来服务器就会在遇到异常时返回 404、500 之类的状态码，比如：
//     * 请求路径不存在的话，是系统异常，如果不拦截的话，服务器会自动返回 404、因为这是客户端搞错路径了
//     * 控制器层的参数校验异常，是系统异常，如果不拦截的话，服务器会自动返回 400、因为这是客户端没按服务端的要求传递参数
//     * 数据库层搞 SQL 语句的异常，是系统异常，如果不拦截的话，服务器会自动返回 500、因为这是服务器端处理出错了
