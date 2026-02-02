package com.ineyee.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.error.SingerServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.SingerMapper;
import com.ineyee.mapper.SongMapper;
import com.ineyee.pojo.dto.SongDetailDto;
import com.ineyee.pojo.dto.SongListDto;
import com.ineyee.pojo.po.Singer;
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
import java.util.Objects;

@Service
@Transactional
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    private final SingerMapper singerMapper;

    public SongServiceImpl(SingerMapper singerMapper) {
        this.singerMapper = singerMapper;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public SongDetailDto get(SongGetQuery query) throws ServiceException {
        Song songPo = getById(query.getId());
        if (songPo == null) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }

        Singer singerPo = singerMapper.selectById(songPo.getSingerId());
        if (singerPo == null) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }

        // po2dto
        return SongDetailDto.from(songPo, singerPo);
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

        // 上面已经泛型了 SongMapper，baseMapper 就是自动注入的 songMapper，不需要我们再手动注入了
        // 直接查出来的就是 dto
        List<SongListDto> list = baseMapper.selectList(queriedPage, query);
        queriedPage.setRecords(list);

        if (query.getPageNum() != null && query.getPageSize() != null) {
            return ListData.fromPage(queriedPage);
        } else {
            return ListData.fromList(list);
        }
    }

    @Override
    public Long save(SongCreateReq req) throws ServiceException {
        // =========== 保存前校验主表——歌手表——里是否存在当前歌手 id ===========
        Singer singer = singerMapper.selectById(req.getSingerId());
        if (singer == null) {
            throw new ServiceException(SingerServiceError.SINGER_NOT_EXIST);
        }
        // =========== 保存前校验主表——歌手表——里是否存在当前歌手 id ===========

        // req2po
        Song entity = new Song();
        BeanUtils.copyProperties(req, entity);
        if (!save(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }

        // po2dto
        return entity.getId();
    }

    @Override
    public List<Long> saveBatch(SongCreateBatchReq req) throws ServiceException {
        // =========== 保存前校验主表——歌手表——里是否存在当前所有的歌手 id ===========
        List<Long> singerIdList = req.getSongList().stream().map(SongCreateReq::getSingerId).toList();
        List<Singer> singerList = singerMapper.selectByIds(singerIdList);
        List<Long> existSingerIdList = singerList.stream().map(Singer::getId).toList();
        List<Long> notExistSingerIdList = new ArrayList<>(singerIdList);
        notExistSingerIdList.removeAll(existSingerIdList);
        if (!notExistSingerIdList.isEmpty()) {
            throw new ServiceException(SingerServiceError.SINGER_NOT_EXIST.getCode(), SingerServiceError.SINGER_NOT_EXIST.getMessage() + notExistSingerIdList);
        }
        // =========== 保存前校验主表——歌手表——里是否存在当前歌手 id ===========

        List<Song> entityList = new ArrayList<>();
        req.getSongList().forEach(item -> {
            // req2po
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
        // =========== 更新前校验主表——歌手表——里是否存在当前歌手 id ===========
        if (req.getSingerId() != null) { // 先看看更新字段里有没有 singerId 字段
            Singer singer = singerMapper.selectById(req.getSingerId());
            if (singer == null) {
                throw new ServiceException(SingerServiceError.SINGER_NOT_EXIST);
            }
        }
        // =========== 更新前校验主表——歌手表——里是否存在当前歌手 id ===========

        // req2po
        Song entity = new Song();
        BeanUtils.copyProperties(req, entity);
        if (!updateById(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void updateBatch(SongUpdateBatchReq req) throws ServiceException {
        // =========== 更新前校验主表——歌手表——里是否存在当前所有的歌手 id ===========
        List<Long> singerIdList = req.getSongList().stream().map(SongUpdateReq::getSingerId).filter(Objects::nonNull).toList();
        if (!singerIdList.isEmpty()) { // 先看看更新字段里有没有 singerId 字段
            List<Singer> singerList = singerMapper.selectByIds(singerIdList);
            List<Long> existSingerIdList = singerList.stream().map(Singer::getId).toList();
            List<Long> notExistSingerIdList = new ArrayList<>(singerIdList);
            notExistSingerIdList.removeAll(existSingerIdList);
            if (!notExistSingerIdList.isEmpty()) {
                throw new ServiceException(SingerServiceError.SINGER_NOT_EXIST.getCode(), SingerServiceError.SINGER_NOT_EXIST.getMessage() + notExistSingerIdList);
            }
        }
        // =========== 更新前校验主表——歌手表——里是否存在当前所有的歌手 id ===========

        List<Song> entityList = new ArrayList<>();
        req.getSongList().forEach(item -> {
            // req2po
            Song entity = new Song();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        });
        if (!updateBatchById(entityList)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }
}
