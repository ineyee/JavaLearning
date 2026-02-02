package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.SingerDetailDto;
import com.ineyee.pojo.dto.SingerListDto;
import com.ineyee.pojo.query.SingerGetQuery;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.SingerCreateReq;
import com.ineyee.pojo.req.SingerCreateBatchReq;
import com.ineyee.pojo.req.SingerDeleteReq;
import com.ineyee.pojo.req.SingerDeleteBatchReq;
import com.ineyee.pojo.req.SingerUpdateReq;
import com.ineyee.pojo.req.SingerUpdateBatchReq;
import com.ineyee.common.api.ListData;
import com.ineyee.service.SingerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/singer")
@Tag(name = "歌手模块")
public class SingerController {
    @Autowired
    private SingerService singerService;

    @GetMapping("/get")
    @Operation(summary = "获取歌手详情")
    public HttpResult<SingerDetailDto> get(@Valid SingerGetQuery query) throws ServiceException {
        SingerDetailDto data = singerService.get(query);
        return HttpResult.ok(data);
    }

    @GetMapping("/list")
    @Operation(summary = "获取歌手列表")
    public HttpResult<ListData<SingerListDto>> list(@Valid SingerListQuery query) {
        ListData<SingerListDto> dataList = singerService.list(query);
        return HttpResult.ok(dataList);
    }

    @PostMapping("/save")
    @Operation(summary = "保存歌手")
    public HttpResult<Long> save(@Valid @RequestBody SingerCreateReq req) throws ServiceException {
        Long id = singerService.save(req);
        return HttpResult.ok(id);
    }

    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存歌手")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody SingerCreateBatchReq req) throws ServiceException {
        List<Long> idList = singerService.saveBatch(req);
        return HttpResult.ok(idList);
    }

    @PostMapping("/remove")
    @Operation(summary = "删除歌手")
    public HttpResult<Void> remove(@Valid @RequestBody SingerDeleteReq req) throws ServiceException {
        singerService.remove(req);
        return HttpResult.ok();
    }

    @PostMapping("/removeBatch")
    @Operation(summary = "批量删除歌手")
    public HttpResult<Void> removeBatch(@Valid @RequestBody SingerDeleteBatchReq req) throws ServiceException {
        singerService.removeBatch(req);
        return HttpResult.ok();
    }

    @PostMapping("/update")
    @Operation(summary = "更新歌手")
    public HttpResult<Void> update(@Valid @RequestBody SingerUpdateReq req) throws ServiceException {
        singerService.update(req);
        return HttpResult.ok();
    }

    @PostMapping("/updateBatch")
    @Operation(summary = "批量更新歌手")
    public HttpResult<Void> updateBatch(@Valid @RequestBody SingerUpdateBatchReq req) throws ServiceException {
        singerService.updateBatch(req);
        return HttpResult.ok();
    }
}
