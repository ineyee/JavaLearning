package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.error.CommonError;
import com.ineyee.common.api.error.UserError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.SingerGetQuery;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.*;
import com.ineyee.pojo.vo.ListData;
import com.ineyee.service.SingerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RestController()
@RequestMapping("/singer")
public class SingerController {
    private SingerService singerService;

    @Autowired
    public void setSingerService(SingerService singerService) {
        this.singerService = singerService;
    }

    @PostMapping("/save")
    public HttpResult<Singer> save(@Valid @RequestBody SingerCreateReq req) throws ServiceException {
        Singer fullSinger = singerService.save(req);
        if (fullSinger != null) {
            return HttpResult.ok(fullSinger);
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    @PostMapping("/saveBatch")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody SingerCreateBatchReq req) throws ServiceException {
        List<Long> singerIdList = singerService.saveBatch(req);
        if (!singerIdList.isEmpty()) {
            return HttpResult.ok(singerIdList);
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    @PostMapping("/remove")
    public HttpResult<Void> remove(@Valid @RequestBody SingerDeleteReq req) throws ServiceException {
        boolean ret = singerService.removeById(req.getId());
        if (ret) {
            return HttpResult.ok();
        } else {
            // 删除失败，肯定是因为用户不存在？感觉这个应该在业务层直接把这个业务先做掉，让业务层抛
            // 上面说的 controller 层抛业务异常，感觉应该在业务层抛，controller 层不抛业务异常、只抛参数校验异常，参数校验成功后、就应该只有给客户端响应成功的代码了（失败是由业务层抛出异常、全局异常处理那里给客户端响应的）
            /*
                •	没有 try-catch
                •	没有 if-else 业务判断
                •	成功就是 ok
                •	失败全部靠异常
            */
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

    @PostMapping("/removeBatch")
    public HttpResult<Void> removeBatch(@Valid @RequestBody SingerDeleteBatchReq req) throws ServiceException {
        boolean ret = singerService.removeBatchByIds(req.getIdList());
        if (ret) {
            return HttpResult.ok();
        } else {
            // 删除失败，肯定是因为有某个用户不存在？肯定是业务上的失败，所以才跑了业务异常？
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

    @PostMapping("/update")
    public HttpResult<Void> update(@Valid @RequestBody SingerUpdateReq req) throws ServiceException {
        Boolean ret = singerService.update(req);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    @PostMapping("/updateBatch")
    public HttpResult<Void> updateBatch(@Valid @RequestBody SingerUpdateBatchReq req) throws ServiceException {
        Boolean ret = singerService.updateBatch(req);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
        }
    }

    @GetMapping("/get")
    public HttpResult<Singer> get(@Valid SingerGetQuery query) throws ServiceException {
        Singer singer = singerService.getById(query.getId());
        if (singer != null) {
            return HttpResult.ok(singer);
        } else {
            throw new ServiceException(UserError.USER_NOT_EXIST.getCode(), UserError.USER_NOT_EXIST.getMessage());
        }
    }

    @GetMapping("/list")
    public HttpResult<ListData<Singer>> list(@Valid SingerListQuery query) throws ServiceException {
        // 这里不需要判断查询结果吗？
        ListData<Singer> singerListData = singerService.list(query);
        return HttpResult.ok(singerListData);
    }
}
