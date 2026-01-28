package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.SingerGetQuery;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.SingerCreateReq;
import com.ineyee.pojo.req.SingerCreateBatchReq;
import com.ineyee.pojo.req.SingerDeleteReq;
import com.ineyee.pojo.req.SingerDeleteBatchReq;
import com.ineyee.pojo.req.SingerUpdateReq;
import com.ineyee.pojo.req.SingerUpdateBatchReq;
import com.ineyee.pojo.vo.ListData;
import com.ineyee.service.SingerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;

    @GetMapping("/get")
    public HttpResult<Singer> get(@Valid SingerGetQuery query) throws ServiceException {
        Singer data = singerService.get(query);
        return HttpResult.ok(data);
    }

    @GetMapping("/list")
    public HttpResult<ListData<Singer>> list(@Valid SingerListQuery query) {
        ListData<Singer> dataList = singerService.list(query);
        return HttpResult.ok(dataList);
    }

    @PostMapping("/save")
    public HttpResult<Singer> save(@Valid @RequestBody SingerCreateReq req) throws ServiceException {
        Singer data = singerService.save(req);
        return HttpResult.ok(data);
    }

    @PostMapping("/saveBatch")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody SingerCreateBatchReq req) throws ServiceException {
        List<Long> idList = singerService.saveBatch(req);
        return HttpResult.ok(idList);
    }

    @PostMapping("/remove")
    public HttpResult<Void> remove(@Valid @RequestBody SingerDeleteReq req) throws ServiceException {
        singerService.remove(req);
        return HttpResult.ok();
    }

    @PostMapping("/removeBatch")
    public HttpResult<Void> removeBatch(@Valid @RequestBody SingerDeleteBatchReq req) throws ServiceException {
        singerService.removeBatch(req);
        return HttpResult.ok();
    }

    @PostMapping("/update")
    public HttpResult<Void> update(@Valid @RequestBody SingerUpdateReq req) throws ServiceException {
        singerService.update(req);
        return HttpResult.ok();
    }

    @PostMapping("/updateBatch")
    public HttpResult<Void> updateBatch(@Valid @RequestBody SingerUpdateBatchReq req) throws ServiceException {
        singerService.updateBatch(req);
        return HttpResult.ok();
    }
}
