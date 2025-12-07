package com.ineyee.controller;

import com.ineyee.domain.Dog;
import com.ineyee.domain.Person;
import com.ineyee.domain.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {
    // 响应体是 JSON 字符串
    // 用 produces 告诉客户端响应体的格式为 JSON 字符串
    // 用 @ResponseBody 注解修饰一下这个方法，这样一来 Spring 框架就会自动把该方法的返回值作为响应体返回给客户端并结束本次请求了
    // 只要项目里添加并配置了 jackson-databind 依赖，其余什么都不需要做，只要该方法的返回值是 Java 对象或 Map，Spring 框架就会自动把该方法的返回值转为 JSON 字符串并返回给客户端了
    //
    // 如果要设置响应头的话，得把返回值搞成 ResponseEntity<T>，然后泛型是我们真正的数据 Response
    @GetMapping(value = "/testJson1", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Response> testJson1() {
        Dog dog1 = new Dog();
        dog1.setName("旺财");
        Dog dog2 = new Dog();
        dog2.setName("二哈");

        Person person = new Person();
        person.setName("张三");
        person.setAge(18);
        person.setHeight(1.88);
        person.setDogList(List.of(dog1, dog2));

        Response response = new Response();
        response.setCode(0);
        response.setMessage("success");
        response.setData(person);

        // 设置响应头
        return ResponseEntity
                .ok()
                .header("X-Custom-Header", "HelloWorld")
                .header("X-Author", "wy")
                .body(response);
    }
}
