package com.ineyee.controller;

import com.ineyee.api.HttpResult;
import com.ineyee.api.error.CommonError;
import com.ineyee.api.error.UserError;
import com.ineyee.api.exception.ServiceException;
import com.ineyee.pojo.po.Singer;
import com.ineyee.service.SingerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;

    /*
     url = http://localhost:9999/tp-dev/singer/get?id=21
     */
    @GetMapping("/get")
    @ResponseBody
    public HttpResult<Singer> get(@RequestParam(value = "id", required = true) Integer id) throws ServiceException {
        Singer user = singerService.get(Long.valueOf(id));
        if (user != null) {
            return HttpResult.ok(user);
        } else {
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

//    /*
//     url = http://localhost:9999/tp-dev/singer/list?pageSize=3&pageNum=2
//     */
//    @GetMapping("/list")
//    @ResponseBody
//    public HttpResult<List<Singer>> list(@RequestParam(value = "pageSize", required = true) Integer pageSize,
//                                       @RequestParam(value = "pageNum", required = true) Integer pageNum) throws ServiceException {
//        List<Singer> userList = singerService.list(pageSize, pageNum);
//        return HttpResult.ok(userList);
//    }
//
//    /*
//     url = http://localhost:9999/tp-dev/singer/list?pageSize=3&pageNum=2
//     */
//    @GetMapping("/listPageHelper")
//    @ResponseBody
//    public HttpResult<List<Singer>> listPageHelper(@RequestParam(value = "pageSize", required = true) Integer pageSize,
//                                                 @RequestParam(value = "pageNum", required = true) Integer pageNum) throws ServiceException {
//        List<Singer> userList = singerService.listPageHelper(pageSize, pageNum);
//        return HttpResult.ok(userList);
//    }

    /*
     url = http://localhost:9999/tp-dev/singer/save
     body = {
         "name": "库里",
         "sex": "男"
     }
     */
    @PostMapping("/save")
    @ResponseBody
    public HttpResult<Singer> save(@Valid @RequestBody Singer user) throws ServiceException {
        Singer fullUser = singerService.save(user);
        if (fullUser != null) {
            return HttpResult.ok(user);
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/tp-dev/singer/saveBatch
     body = { // 最好再包一层，这样接受参数的时候好接收
        "singerList": [
         {
             "name": "三三",
             "sex": "女"
         },
         {
             "name": "四四",
             "sex": "男"
         },
         {
             "name": "五五"
         }
     ]
          body = [
         {
             "name": "三三",
             "sex": "女"
         },
         {
             "name": "四四",
             "sex": "男"
         },
         {
             "name": "五五"
         }
     ]
     }
     */
    @PostMapping("/saveBatch")
    @ResponseBody
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody List<Singer> userList) throws ServiceException {
        List<Long> ret = singerService.saveBatch(userList);
        if (!ret.isEmpty()) {
            return HttpResult.ok(ret);
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/tp-dev/singer/remove
     body = {
        "id": 1
     }
     */
    @PostMapping("/remove")
    @ResponseBody
    public HttpResult<Void> remove(@RequestBody Map<String, Object> params) throws ServiceException {
        Boolean ret = singerService.remove(((Integer) params.get("id")).longValue());
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

    /*
     url = http://localhost:9999/tp-dev/singer/removeBatch
     body = {
        "idList": [1, 2, 54, 55]
     }
     */
    @PostMapping("/removeBatch")
    @ResponseBody
    public HttpResult<Void> removeBatch(@RequestBody Map<String, Object> params) throws ServiceException {
        Boolean ret = singerService.removeBatch((List<Long>) params.get("idList"));
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
    url = http://localhost:9999/tp-dev/singer/update
    body = {
       "id": 1,
       "age": 31
    }
    */
    @PostMapping("/update")
    @ResponseBody
    public HttpResult<Void> update(@Valid @RequestBody Singer singer) throws ServiceException {
        Boolean ret = singerService.update(singer);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    /*
     url = http://localhost:9999/tp-dev/singer/updateBatch
     body = [ // 最好再包一层，这样接受参数的时候好接收
        {
           "id": 1,
           "name": "张三"
        },
        {
           "id": 2,
           "sex": "女"
        }
     ]
    body = {
        "singerList": [
        {
           "id": 1,
           "name": "张三"
        },
        {
           "id": 2,
           "sex": "女"
        }
     ]
    }
     */
    @PostMapping("/updateBatch")
    @ResponseBody
    public HttpResult<Void> updateBatch(@RequestBody List<Singer> singerList) throws ServiceException {
        Boolean ret = singerService.updateBatch(singerList);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }
}
