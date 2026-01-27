package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.error.UserServiceError;
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
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @PostMapping("/saveBatch")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody SingerCreateBatchReq req) throws ServiceException {
        List<Long> singerIdList = singerService.saveBatch(req);
        if (!singerIdList.isEmpty()) {
            return HttpResult.ok(singerIdList);
        } else {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
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
            throw new ServiceException(UserServiceError.USER_NOT_EXIST);
        }
    }

    @PostMapping("/removeBatch")
    public HttpResult<Void> removeBatch(@Valid @RequestBody SingerDeleteBatchReq req) throws ServiceException {
        boolean ret = singerService.removeBatchByIds(req.getIdList());
        if (ret) {
            return HttpResult.ok();
        } else {
            // 删除失败，肯定是因为有某个用户不存在？肯定是业务上的失败，所以才跑了业务异常？
            throw new ServiceException(UserServiceError.USER_NOT_EXIST);
        }
    }

    @PostMapping("/update")
    public HttpResult<Void> update(@Valid @RequestBody SingerUpdateReq req) throws ServiceException {
        Boolean ret = singerService.update(req);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @PostMapping("/updateBatch")
    public HttpResult<Void> updateBatch(@Valid @RequestBody SingerUpdateBatchReq req) throws ServiceException {
        Boolean ret = singerService.updateBatch(req);
        if (ret) {
            return HttpResult.ok();
        } else {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @GetMapping("/get")
    public HttpResult<Singer> get(@Valid SingerGetQuery query) throws ServiceException {
        Singer singer = singerService.getById(query.getId());
        if (singer != null) {
            return HttpResult.ok(singer);
        } else {
            throw new ServiceException(UserServiceError.USER_NOT_EXIST);
        }
    }

    @GetMapping("/list")
    public HttpResult<ListData<Singer>> list(@Valid SingerListQuery query) throws ServiceException {
        // 这里不需要判断查询结果吗？
        ListData<Singer> singerListData = singerService.list(query);
        return HttpResult.ok(singerListData);
    }
}
