package com.ineyee.api.exception;

import com.ineyee.api.HttpResult;
import com.ineyee.api.error.CommonError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 第一步：自定义一个 GlobalExceptionHandler 类
// 用 @ControllerAdvice 注解修饰一下这个类，这个注解有两个用途，一是放到 IoC 容器里，二是通过 AOP 把异常处理代码切入到 controller 层的方法里
@ControllerAdvice
public class GlobalExceptionHandler {
    // 第二步：自定义一个方法
    // 用 @ExceptionHandler 注解修饰一下这个方法，表明这个方法专门用来处理 ServiceException 业务异常
    // 这里是业务异常 ServiceException，业务异常各有各的错误码和错误信息，用户在客户端看到错误信息是有提示意义的
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public HttpResult<Void> handleServiceException(ServiceException e) {
        // 给客户端响应错误
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    // 这个方法专门用来处理业务异常以外的系统异常
    // 这里是系统异常 Exception，系统异常是没有 code 的，而且系统异常五花八门、我们也不知道它们什么时候会抛出、抛出的错误信息对用户来说也没有提示意义
    // 所以我们把系统异常的错误码统一为 -100000，错误信息统一为请求失败
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResult<Void> handleException(Exception e) {
        // 给客户端响应错误
        return HttpResult.error(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
    }
}
