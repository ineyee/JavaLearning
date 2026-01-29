package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

import java.util.List;

public interface SongService extends IService<Song> {
    Song get(SongGetQuery query) throws ServiceException;

    ListData<SongListDto> list(SongListQuery query);

    Song save(SongCreateReq req) throws ServiceException;

    List<Long> saveBatch(SongCreateBatchReq req) throws ServiceException;

    void remove(SongDeleteReq req) throws ServiceException;

    void removeBatch(SongDeleteBatchReq req) throws ServiceException;

    void update(SongUpdateReq req) throws ServiceException;

    void updateBatch(SongUpdateBatchReq req) throws ServiceException;
}
