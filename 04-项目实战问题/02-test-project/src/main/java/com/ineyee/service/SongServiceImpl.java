package com.ineyee.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.SongMapper;
import com.ineyee.pojo.dto.SongListDto;
import com.ineyee.pojo.po.Song;
import com.ineyee.pojo.query.SongGetQuery;
import com.ineyee.pojo.query.SongListQuery;
import com.ineyee.pojo.req.SongCreateBatchReq;
import com.ineyee.pojo.req.SongCreateReq;
import com.ineyee.pojo.req.SongDeleteBatchReq;
import com.ineyee.pojo.req.SongDeleteReq;
import com.ineyee.pojo.req.SongUpdateBatchReq;
import com.ineyee.pojo.req.SongUpdateReq;
import com.ineyee.common.api.ListData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Song get(SongGetQuery query) throws ServiceException {
        Song data = getById(query.getId());
        if (data == null) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ListData<SongListDto> list(SongListQuery query) {
        Page<SongListDto> queriedPage = new Page<>();

        if (query.getPageNum() != null && query.getPageSize() != null) {
            queriedPage.setCurrent(query.getPageNum());
            queriedPage.setSize(query.getPageSize());
        } else {
            queriedPage.setCurrent(1);
            queriedPage.setSize(Long.MAX_VALUE);
        }

        List<SongListDto> list = baseMapper.selectList(queriedPage, query);
        queriedPage.setRecords(list);

        if (query.getPageNum() != null && query.getPageSize() != null) {
            return ListData.fromPage(queriedPage);
        } else {
            return ListData.fromList(list);
        }
    }

    @Override
    public Song save(SongCreateReq req) throws ServiceException {
        Song entity = new Song();
        BeanUtils.copyProperties(req, entity);
        if (!save(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return entity;
    }

    @Override
    public List<Long> saveBatch(SongCreateBatchReq req) throws ServiceException {
        List<Song> entityList = new ArrayList<>();
        req.getSongList().forEach(item -> {
            Song entity = new Song();
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
    public void remove(SongDeleteReq req) throws ServiceException {
        if (!removeById(req.getId())) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void removeBatch(SongDeleteBatchReq req) throws ServiceException {
        if (!removeBatchByIds(req.getIdList())) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void update(SongUpdateReq req) throws ServiceException {
        Song entity = new Song();
        BeanUtils.copyProperties(req, entity);
        if (!updateById(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void updateBatch(SongUpdateBatchReq req) throws ServiceException {
        List<Song> entityList = new ArrayList<>();
        req.getSongList().forEach(item -> {
            Song entity = new Song();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        });
        if (!updateBatchById(entityList)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }
}
