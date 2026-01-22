package com.ineyee.common.api.exception;

import com.ineyee.common.api.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 第一步：自定义一个 GlobalExceptionHandler 类
// 用 @ControllerAdvice 注解修饰一下这个类，这个注解有两个用途，一是放到 IoC 容器里，二是通过 AOP 把异常处理代码切入到 controller 层的方法里
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 第二步：自定义一个方法
    // 用 @ExceptionHandler 注解修饰一下这个方法，表明这个方法专门用来处理 ServiceException 业务异常
    // 这里是业务异常 ServiceException，业务异常各有各的错误码和错误信息，用户在客户端看到错误信息是有提示意义的
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public HttpResult<Void> handleServiceException(ServiceException e) {
        // 给客户端响应错误
        log.warn("业务异常：{}", e.toString());
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    // 用 @ExceptionHandler 注解修饰一下这个方法，表明这个方法专门用来处理业务异常以外的系统异常
    // 这里是系统异常 Exception，系统异常是没有 code 的、所以我们把系统异常的错误码统一为 -100000，错误信息自动获取、当然客户端不一定会把这个信息展示给用户看、但在联调接口时非常有用
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResult<Void> handleException(Exception e) {
        // 给客户端响应错误
        log.error("系统异常：{}", e.getMessage());
        return HttpResult.error(e.getMessage());
    }
}
