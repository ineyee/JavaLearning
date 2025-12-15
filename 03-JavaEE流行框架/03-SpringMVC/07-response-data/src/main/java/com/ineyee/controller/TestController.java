package com.ineyee.controller;

import com.ineyee.domain.Dog;
import com.ineyee.domain.Person;
import com.ineyee.api.HttpResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {
    // 响应体是普通文本
    // 用 produces 告诉客户端响应体的格式为普通文本
    // 用 @ResponseBody 注解修饰一下这个方法，这样一来 Spring 框架就会自动把该方法的返回值作为响应体返回给客户端并结束本次请求了
    @GetMapping(value = "/testPlain", produces = "text/plain")
    @ResponseBody
    public String testPlain() {
        return "<h1>登录成功<h1>";
    }

    // 响应体是 HTML 文本
    // 用 produces 告诉客户端响应体的格式为 HTML 文本
    // 用 @ResponseBody 注解修饰一下这个方法，这样一来 Spring 框架就会自动把该方法的返回值作为响应体返回给客户端并结束本次请求了
    @GetMapping(value = "/testHtml", produces = "text/html")
    @ResponseBody
    public String testHtml() {
        return "<h1>登录成功<h1>";
    }

    // 响应体是 JSON 字符串
    // 用 produces 告诉客户端响应体的格式为 JSON 字符串
    // 用 @ResponseBody 注解修饰一下这个方法，这样一来 Spring 框架就会自动把该方法的返回值作为响应体返回给客户端并结束本次请求了
    //
    // 只要项目里添加并配置了 jackson-databind 依赖，其余什么都不需要做，只要该方法的返回值是 Java 对象或 Map，Spring 框架就会自动把该方法的返回值转为 JSON 字符串并返回给客户端了
    // 需要自定义响应码和响应头时，可以给 Response 包裹一个 ResponseEntity 作为返回值，不需要自定义响应码和响应头时，直接返回即可
    @GetMapping(value = "/testJson1", produces = "application/json")
    @ResponseBody
    public HttpResult<Person> testJson1() {
        Dog dog1 = new Dog();
        dog1.setName("旺财");
        Dog dog2 = new Dog();
        dog2.setName("二哈");

        Person person = new Person();
        person.setName("张三");
        person.setAge(18);
        person.setHeight(1.88);
        person.setDogList(List.of(dog1, dog2));

        return HttpResult.ok(person);
    }

    @GetMapping(value = "/testJson2", produces = "application/json")
    @ResponseBody
    public Map<String, Object> testJson2() {
        Dog dog1 = new Dog();
        dog1.setName("旺财");
        Dog dog2 = new Dog();
        dog2.setName("二哈");

        Person person = new Person();
        person.setName("张三");
        person.setAge(18);
        person.setHeight(1.88);
        person.setDogList(List.of(dog1, dog2));

        Map<String, Object> httpResult = new HashMap<>();
        httpResult.put("code", 0);
        httpResult.put("message", "success");
        httpResult.put("data", person);

        return httpResult;
    }
}
