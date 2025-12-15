package com.ineyee.controller;

import com.ineyee.dto.UserCreateDto;
import com.ineyee.dto.UserUpdateDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    // 假设客户端发送的 post 请求为：
    // url = http://localhost:9999/{{applicationContext}}/test01
    // body = partyMember=true&age=18&height=1.88&salary=11.21&name=ineyee
    @PostMapping(value = "/test01")
    @ResponseBody
    // 使用 @RequestParam 注解一个参数一个参数获取
    //     value：post 请求里的参数名，可能跟我们 test01 方法里的参数名不同
    //     required：参数是否必传，默认为 true
    public String test01(@RequestParam(value = "partyMember", required = true) Boolean isPartyMember,
                         @RequestParam(value = "age", required = false) Integer age,
                         @RequestParam(value = "height", required = false) Double height,
                         @RequestParam(value = "salary", required = false) BigDecimal salary,
                         @RequestParam(value = "name", required = true) String name) {
        return "test01 success = " + isPartyMember + " " + age + " " + height + " " + salary + " " + name;
    }

    // 假设客户端发送的 post 请求为：
    // url = http://localhost:9999/{{applicationContext}}/test02
    // body = partyMember=true&age=18&height=1.88&salary=11.21&name=ineyee
    @PostMapping("/test02")
    @ResponseBody
    // 使用 @Valid 注解对参数进行校验
    public String test02(@Valid UserCreateDto userCreateDto) {
        return "test02 success = " + userCreateDto;
    }

    // 假设客户端发送的 post 请求为：
    // url = http://localhost:9999/{{applicationContext}}/test03
    // body = partyMember=true&age=18&height=1.88&salary=11.21&name=ineyee
    @PostMapping("/test03")
    @ResponseBody
    // 使用 @RequestParam 注解获取参数
    public String test03(@RequestParam Map<String, Object> params) {
        return "test03 success = " + params;
    }

    // 假设客户端发送的 post 请求为：
    // url = http://localhost:9999/{{applicationContext}}/test04
    // body = ids=1&ids=2&ids=3
    @PostMapping("/test04")
    @ResponseBody
    // 使用 @RequestParam 注解获取参数
    public String test04(@RequestParam("ids") List<String> ids) {
        return "test04 success = " + ids;
    }

    // 假设客户端发送的 post 请求为：
    // url = http://localhost:9999/{{applicationContext}}/test05
    // body = {"partyMember":true,"age":18,"height":1.88,"salary":11.21,"name":"ineyee"}
    @PostMapping("/test05")
    @ResponseBody
    // 使用 @RequestBody 注解获取参数
    // 使用 @Valid 注解对参数进行校验
    //
    // 只要项目里添加并配置了 jackson-databind 依赖，其余什么都不需要做，只要该方法的参数是 Java 对象或 Map，Spring 框架就会自动把客户端请求体的 JSON 字符串转为 Java 对象或 Map
    public String test05(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return "test05 success = " + userUpdateDto;
    }

    // 假设客户端发送的 post 请求为：
    // url = http://localhost:9999/{{applicationContext}}/test06
    // body = {"ids": [1, 2, 3]}
    //
    // 只要项目里添加并配置了 jackson-databind 依赖，其余什么都不需要做，只要该方法的参数是 Java 对象或 Map，Spring 框架就会自动把客户端请求体的 JSON 字符串转为 Java 对象或 Map
    @PostMapping("/test06")
    @ResponseBody
    // 使用 @RequestBody 注解获取参数
    public String test06(@RequestBody Map<String, Object> params) {
        return "test06 success = " + params.get("ids");
    }
}
