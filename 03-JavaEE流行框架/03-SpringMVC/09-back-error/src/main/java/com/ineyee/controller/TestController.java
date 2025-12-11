package com.ineyee.controller;

import com.ineyee.exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 前面《02-JavaWeb基础：05-ProjectArchitecture》里我们提到过：
// * dao 层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
// * service 层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
// * controller 层调用 service 层的 API 时一定要用 try-catch，因为 dao 层和 service 层的异常它们都没处理、都是继续上抛到 controller 层来统一处理的，catch 到异常时就给客户端响应错误，没有错误时就给客户端响应数据
//
// 这么做是非常合理的，但是有点麻烦，因为我们的项目里会有很多个 controller，每个 controller 里又会有很多个接口方法
// 如果要在每个接口方法里都写 try-catch，代码量就会非常大，而且给客户端响应错误的代码非常相似，就会写大量重复的代码
//
// 所以我们现在要实现一种新方案：
// * controller 层调用 service 层的 API 时直接调用就可以了，不用再 try-catch，把异常继续往上抛即可
// * controller 层把异常继续往上抛，就会抛给 DispatcherServlet，DispatcherServlet 捕获到异常就会调用我们定义的异常处理器来统一处理异常、给客户端响应错误
@Controller
public class TestController {
    @GetMapping("/test01")
    @ResponseBody
    public void test01() throws Exception {
        System.out.println(111111);
        // 假设这里是调用 service 层的 API 时，抛出了业务异常
        throw new ServiceException(-1001, "invalid token");
    }

    @GetMapping("/test02")
    @ResponseBody
    public void test02() throws Exception {
        // 假设这里是调用 service 层的 API 时，抛出了系统异常
        throw new ClassNotFoundException("TestClass");
    }
}
