package com.ineyee.controller;

import com.ineyee.controller.param.UserQueryParam;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Controller
public class GetController {
    // 假设客户端发送的 get 请求为：http://localhost:9999/{{applicationContext}}/test01?partyMember=true&age=18&height=1.88&salary=11.21&name=ineyee
    @GetMapping(value = "/test01")
    @ResponseBody
    // 使用 @RequestParam 注解一个参数一个参数获取
    //     value：get 请求里的参数名，可能跟我们 test01 方法里的参数名不同
    //     required：参数是否必传，默认为 true
    public String test01(@RequestParam(value = "partyMember", required = true) Boolean isPartyMember,
                         @RequestParam(value = "age", required = false) Integer age,
                         @RequestParam(value = "height", required = false) Double height,
                         @RequestParam(value = "salary", required = false) BigDecimal salary,
                         @RequestParam(value = "name", required = true) String name) {
        return "test01 success = " + isPartyMember + " " + age + " " + height + " " + salary + " " + name;
    }

    // 假设客户端发送的 get 请求为：http://localhost:9999/{{applicationContext}}/test02?partyMember=true&age=18&height=1.88&salary=11.21&name=ineyee
    @GetMapping("/test02")
    @ResponseBody
    // 使用 @Valid 注解对参数进行校验
    public String test02(@Valid UserQueryParam userQueryParam) {
        return "test02 success = " + userQueryParam;
    }

    // 假设客户端发送的 get 请求为：http://localhost:9999/{{applicationContext}}/test03?partyMember=true&age=18&height=1.88&salary=11.21&name=ineyee
    @GetMapping("/test03")
    @ResponseBody
    // 使用 @RequestParam 注解获取参数
    public String test03(@RequestParam Map<String, Object> params) {
        return "test03 success = " + params;
    }

    // 假设客户端发送的 get 请求为：http://localhost:9999/{{applicationContext}}/test04?ids=1&ids=2&ids=3
    @GetMapping("/test04")
    @ResponseBody
    // 使用 @RequestParam 注解获取参数
    public String test04(@RequestParam("ids") List<String> ids) {
        return "test04 success = " + ids;
    }

    // 假设客户端发送的 get 请求为：http://localhost:9999/{{applicationContext}}/test05/123456，其中 123456 为 id 参数的值
    @GetMapping("/test05/{id}")
    @ResponseBody
    // 使用 @PathVariable 注解获取参数
    //     value：get 请求里的参数名，可能跟我们 test05 方法里的参数名不同
    //     required：参数是否必传，默认为 true
    public String test05(@PathVariable(value = "id", required = true) String id) {
        return "test05 success = " + id;
    }
}
