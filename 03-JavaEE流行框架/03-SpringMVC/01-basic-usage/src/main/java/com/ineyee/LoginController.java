package com.ineyee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// 第一步：创建一个 controller 类，不需要继承自任何父类
// 约定俗成地我们会把这个类命名为 XxxController
//
// 第二步：用 @Controller 注解修饰一下这个类，以便 Spring 框架能自动创建一个该类的对象并放到 IoC 容器里
// 对象默认的 beanId 就是它所属类名的第一个字母变成小写（即 loginController），当然我们也可以通过 value 属性来自定义 beanId（value 属性名可以省略不写，直接写值）
@Controller
public class LoginController {
    // 第三步：定义一个方法
    // 用 @RequestMapping(path = requestPath, method = requestMethod) 注解修饰这个方法来指定请求路径和请求方法，以便 Spring 框架能把来自客户端的请求精准分发到该方法
    //     path 属性用来指定请求路径，注意 / 不能少
    //     method 属性用来指定请求方法，默认是 Get
    // 用 @ResponseBody 注解修饰一下这个方法，这样一来 Spring 框架就会自动把该方法的返回值作为响应体返回给客户端并结束本次请求了
    @RequestMapping(path = "/login01", method = RequestMethod.GET)
    @ResponseBody
    public String login1() {
        return "get success";
    }

    @RequestMapping(path = "/login01", method = RequestMethod.POST)
    @ResponseBody
    public String login2() {
        return "post success";
    }
}

// 第四步：点击 Run 或 Debug 启动 Tomcat，Tomcat 就会把我们的 JavaWeb 项目给自动部署好
// 我们已经设置了 Tomcat 监听 9999 端口，启动 Tomcat 时会启动一个本地服务器
//
// 打开 Postman，访问 http://localhost:9999/springmvc/login01 就可以了
// 注意：每次修改了服务器的代码后，都需要重新部署甚至重启服务器，否则服务器不会自动更新
