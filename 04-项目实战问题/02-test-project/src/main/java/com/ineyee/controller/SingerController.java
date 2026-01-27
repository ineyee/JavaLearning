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
        ListData<Singer> singerListData = singerService.list(query);
        return HttpResult.ok(singerListData);
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
            throw new ServiceException(UserServiceError.USER_NOT_EXIST);
        }
    }

    @PostMapping("/removeBatch")
    public HttpResult<Void> removeBatch(@Valid @RequestBody SingerDeleteBatchReq req) throws ServiceException {
        boolean ret = singerService.removeBatchByIds(req.getIdList());
        if (ret) {
            return HttpResult.ok();
        } else {
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
}
