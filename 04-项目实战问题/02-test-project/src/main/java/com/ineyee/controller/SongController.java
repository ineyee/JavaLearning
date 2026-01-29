package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.SongListDto;
import com.ineyee.pojo.po.Song;
import com.ineyee.pojo.query.SongGetQuery;
import com.ineyee.pojo.query.SongListQuery;
import com.ineyee.pojo.req.SongCreateReq;
import com.ineyee.pojo.req.SongCreateBatchReq;
import com.ineyee.pojo.req.SongDeleteReq;
import com.ineyee.pojo.req.SongDeleteBatchReq;
import com.ineyee.pojo.req.SongUpdateReq;
import com.ineyee.pojo.req.SongUpdateBatchReq;
import com.ineyee.common.api.ListData;
import com.ineyee.service.SongService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/song")
public class SongController {
    @Autowired
    private SongService songService;

    @GetMapping("/get")
    public HttpResult<Song> get(@Valid SongGetQuery query) throws ServiceException {
        Song data = songService.get(query);
        return HttpResult.ok(data);
    }

    @GetMapping("/list")
    public HttpResult<ListData<SongListDto>> list(@Valid SongListQuery query) {
        ListData<SongListDto> dataList = songService.list(query);
        return HttpResult.ok(dataList);
    }

    @PostMapping("/save")
    public HttpResult<Song> save(@Valid @RequestBody SongCreateReq req) throws ServiceException {
        Song data = songService.save(req);
        return HttpResult.ok(data);
    }

    @PostMapping("/saveBatch")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody SongCreateBatchReq req) throws ServiceException {
        List<Long> idList = songService.saveBatch(req);
        return HttpResult.ok(idList);
    }

    @PostMapping("/remove")
    public HttpResult<Void> remove(@Valid @RequestBody SongDeleteReq req) throws ServiceException {
        songService.remove(req);
        return HttpResult.ok();
    }

    @PostMapping("/removeBatch")
    public HttpResult<Void> removeBatch(@Valid @RequestBody SongDeleteBatchReq req) throws ServiceException {
        songService.removeBatch(req);
        return HttpResult.ok();
    }

    @PostMapping("/update")
    public HttpResult<Void> update(@Valid @RequestBody SongUpdateReq req) throws ServiceException {
        songService.update(req);
        return HttpResult.ok();
    }

    @PostMapping("/updateBatch")
    public HttpResult<Void> updateBatch(@Valid @RequestBody SongUpdateBatchReq req) throws ServiceException {
        songService.updateBatch(req);
        return HttpResult.ok();
    }
}
