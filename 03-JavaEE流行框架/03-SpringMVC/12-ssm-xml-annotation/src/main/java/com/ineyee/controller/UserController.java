package com.ineyee.controller;

import com.ineyee.api.HttpResult;
import com.ineyee.api.error.CommonError;
import com.ineyee.api.error.UserError;
import com.ineyee.api.exception.ServiceException;
import com.ineyee.domain.User;
import com.ineyee.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// 用户模块接口的表现层之控制器层
//
// 控制器层（controller）的职责就是直接与客户端打交道，即：
//   * 接收客户端的请求参数，对客户端的请求参数进行基础有效性校验（在表现层进行请求参数的基础有效性校验是为了在请求进入业务逻辑前就拒绝非法格式，避免无效数据渗透到下层，主要包含：必填字段校验、字段长度校验、字段格式校验等，其它跟业务相关的校验就交给业务层去校验）
//   * 调用业务层的 API 拿到业务型数据并转换成客户端直接能用的 JSON、HTML 等数据，给客户端返回响应
//
// 换句话说：
//   * 对下，表现层要对业务层负责，负责的体现就是要做好请求参数的基础有效性校验，确保传递给业务层的参数是人家直接能用的，业务层只需要在此基础上附加一层业务规则校验就可以了
//   * 对上，表现层要对客户端负责，负责的体现就是要做好数据转换，确保返回给客户端的数据是人家直接能用的 JSON、HTML 等数据或错误信息
//   * 其它的表现层就不用关心了
//
// 实践经验：
//   * 接口到底是请求成功还是请求失败，统一在表现层处理，失败了就给客户端响应错误，成功了就给客户端响应数据
//   * 基础有效性校验出错时，需要直接给客户端响应错误
//   * 调用业务层的 API 时一定要用 try-catch，因为数据层和业务层的异常它们都没处理、都是继续上抛到表现层来统一处理的，catch 到异常时就给客户端响应错误，没有错误时就给客户端响应数据
// 但是因为现在有了全局处理异常、给客户端响应错误，所以：
//   * controller 层调用 service 层的 API 时直接调用就可以了，不用再 try-catch，如果遇到了系统异常那就把系统异常继续往上抛，如果遇到了业务异常那就主动抛出个业务异常
//   * controller 层把异常继续往上抛，就会抛给 DispatcherServlet，DispatcherServlet 捕获到异常就会调用我们定义的异常处理器来统一处理异常、给客户端响应错误
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /*
     url = http://localhost:9999/helloMyBatis/user/get?id=21
     */
    @GetMapping("/get")
    @ResponseBody
    public HttpResult<User> get(@RequestParam(value = "id", required = true) Integer id) throws ServiceException {
        User user = userService.get(id);
        if (user != null) {
            return HttpResult.ok(user);
        } else {
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/list?pageSize=3&pageNum=2
     */
    @GetMapping("/list")
    @ResponseBody
    public HttpResult<List<User>> list(@RequestParam(value = "pageSize", required = true) Integer pageSize,
                                       @RequestParam(value = "pageNum", required = true) Integer pageNum) throws ServiceException {
        List<User> userList = userService.list(pageSize, pageNum);
        return HttpResult.ok(userList);
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/list?pageSize=3&pageNum=2
     */
    @GetMapping("/listPageHelper")
    @ResponseBody
    public HttpResult<List<User>> listPageHelper(@RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                 @RequestParam(value = "pageNum", required = true) Integer pageNum) throws ServiceException {
        List<User> userList = userService.listPageHelper(pageSize, pageNum);
        return HttpResult.ok(userList);
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/save
     body = {
        "user": {
            "name": "库里",
            "age": 37,
            "height": 1.88,
            "email": "curry@qq.com"
        }
     }
     */
    @PostMapping("/save")
    @ResponseBody
    public HttpResult<User> save(@Valid @RequestBody User user) throws ServiceException {
        User fullUser = userService.save(user);
        if (fullUser != null) {
            return HttpResult.ok(user);
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/saveBatch
     body = {
         "userList": [
             {
                 "name": "三三",
                 "age": 18,
                 "height": 1.88,
                 "email": "sansan@qq.com"
             },
             {
                 "name": "四四",
                 "age": 19,
                 "height": 1.99,
                 "email": "sisi@qq.com"
             },
             {
                 "name": "五五",
                 "age": 20,
                 "height": 2.00,
                 "email": "wuwu@qq.com"
             }
         ]
     }
     */
    @PostMapping("/saveBatch")
    @ResponseBody
    public HttpResult<List<Integer>> saveBatch(@Valid @RequestBody List<User> userList) throws ServiceException {
        List<Integer> ret = userService.saveBatch(userList);
        if (!ret.isEmpty()) {
            return HttpResult.ok(ret);
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/remove
     body = {
        "id": 1
     }
     */
    @PostMapping("/remove")
    @ResponseBody
    public HttpResult<Void> remove(@RequestBody Integer id) throws ServiceException {
        Boolean ret = userService.remove(id);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/removeBatch
     body = {
        "idList": [1, 2, 54, 55]
     }
     */
    @PostMapping("/removeBatch")
    @ResponseBody
    public HttpResult<Void> removeBatch(@RequestBody List<Integer> idList) throws ServiceException {
        Boolean ret = userService.removeBatch(idList);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/update
     body = {
        "id": 1,
        "fieldsToUpdate": {
            "age": 31
        }
     }
     */
    public HttpResult<Void> update(@RequestBody Integer id, @RequestBody Map<String, Object> fieldsToUpdate) throws ServiceException {
        Boolean ret = userService.update(id, fieldsToUpdate);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/updateBatch
     body = {
        "idList": [1, 2, 54, 55],
        "fieldsToUpdate": {
            "age": 31
        }
     }
     */
    public HttpResult<Void> updateBatch(@RequestBody List<Integer> idList, @RequestBody Map<String, Object> fieldsToUpdate) throws ServiceException {
        Boolean ret = userService.updateBatch(idList, fieldsToUpdate);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }
}
