package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.SingerDetailDto;
import com.ineyee.pojo.dto.SingerListDto;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.SingerGetQuery;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.SingerCreateReq;
import com.ineyee.pojo.req.SingerCreateBatchReq;
import com.ineyee.pojo.req.SingerDeleteReq;
import com.ineyee.pojo.req.SingerDeleteBatchReq;
import com.ineyee.pojo.req.SingerUpdateReq;
import com.ineyee.pojo.req.SingerUpdateBatchReq;
import com.ineyee.common.api.ListData;

import java.util.List;

public interface SingerService extends IService<Singer> {
    SingerDetailDto get(SingerGetQuery query) throws ServiceException;

    ListData<SingerListDto> list(SingerListQuery query);

    Long save(SingerCreateReq req) throws ServiceException;

    List<Long> saveBatch(SingerCreateBatchReq req) throws ServiceException;

    void remove(SingerDeleteReq req) throws ServiceException;

    void removeBatch(SingerDeleteBatchReq req) throws ServiceException;

    void update(SingerUpdateReq req) throws ServiceException;

    void updateBatch(SingerUpdateBatchReq req) throws ServiceException;
}
