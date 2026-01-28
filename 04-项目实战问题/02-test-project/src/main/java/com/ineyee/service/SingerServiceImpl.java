package com.ineyee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.SingerMapper;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.SingerGetQuery;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.SingerCreateBatchReq;
import com.ineyee.pojo.req.SingerCreateReq;
import com.ineyee.pojo.req.SingerDeleteBatchReq;
import com.ineyee.pojo.req.SingerDeleteReq;
import com.ineyee.pojo.req.SingerUpdateBatchReq;
import com.ineyee.pojo.req.SingerUpdateReq;
import com.ineyee.pojo.vo.ListData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Singer get(SingerGetQuery query) throws ServiceException {
        Singer data = getById(query.getId());
        if (data == null) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ListData<Singer> list(SingerListQuery query) {
        LambdaQueryWrapper<Singer> wrapper = new LambdaQueryWrapper<>();
        // TODO: 按需追加查询条件

        wrapper.orderByDesc(Singer::getCreateTime)
                .orderByDesc(Singer::getId);

        if (query.getPageNum() != null && query.getPageSize() != null) {
            Page<Singer> page = new Page<>(query.getPageNum(), query.getPageSize());
            Page<Singer> queriedPage = page(page, wrapper);
            return ListData.fromPage(queriedPage);
        } else {
            List<Singer> dataList = list(wrapper);
            return ListData.fromList(dataList);
        }
    }

    @Override
    public Singer save(SingerCreateReq req) throws ServiceException {
        Singer entity = new Singer();
        BeanUtils.copyProperties(req, entity);
        if (!save(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return entity;
    }

    @Override
    public List<Long> saveBatch(SingerCreateBatchReq req) throws ServiceException {
        List<Singer> entityList = new ArrayList<>();
        req.getSingerList().forEach(item -> {
            Singer entity = new Singer();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        });
        if (!saveBatch(entityList)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        List<Long> idList = new ArrayList<>();
        entityList.forEach(item -> idList.add(item.getId()));
        return idList;
    }

    @Override
    public void remove(SingerDeleteReq req) throws ServiceException {
        if (!removeById(req.getId())) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void removeBatch(SingerDeleteBatchReq req) throws ServiceException {
        if (!removeBatchByIds(req.getIdList())) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void update(SingerUpdateReq req) throws ServiceException {
        Singer entity = new Singer();
        BeanUtils.copyProperties(req, entity);
        if (!updateById(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void updateBatch(SingerUpdateBatchReq req) throws ServiceException {
        List<Singer> entityList = new ArrayList<>();
        req.getSingerList().forEach(item -> {
            Singer entity = new Singer();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        });
        if (!updateBatchById(entityList)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }
}
