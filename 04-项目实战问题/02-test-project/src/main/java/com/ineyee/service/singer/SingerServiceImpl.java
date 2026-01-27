package com.ineyee.service.singer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.SingerMapper;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.singer.SingerListQuery;
import com.ineyee.pojo.req.singer.SingerCreateBatchReq;
import com.ineyee.pojo.req.singer.SingerCreateReq;
import com.ineyee.pojo.req.singer.SingerUpdateBatchReq;
import com.ineyee.pojo.req.singer.SingerUpdateReq;
import com.ineyee.pojo.vo.ListData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {
    @Override
    public Singer save(SingerCreateReq req) throws ServiceException {
        Singer singer = new Singer();
        singer.setName(req.getName());
        singer.setSex(req.getSex());

        // 这里调用 MyBatis-Plus 在 service 层提供的 save 方法，不再直接调用 mapper 层的 insert 方法
        boolean success = save(singer);

        return success ? singer : null;
    }

    @Override
    public List<Long> saveBatch(SingerCreateBatchReq req) throws ServiceException {
        List<Singer> singerList = new ArrayList<>();
        req.getSingerList().forEach(item -> {
            Singer singer = new Singer();
            singer.setName(item.getName());
            singer.setSex(item.getSex());
            singerList.add(singer);
        });

        // 这里调用 MyBatis-Plus 在 service 层提供的 saveBatch 方法，不再直接调用 mapper 层的 insertBatch 方法
        boolean success = saveBatch(singerList);

        List<Long> singerIdList = new ArrayList<>();
        if (success) {
            singerList.forEach(item -> {
                singerIdList.add(item.getId());
            });
        }

        return singerIdList;
    }

    @Override
    public Boolean update(SingerUpdateReq req) throws ServiceException {
        Singer singer = new Singer();
        singer.setId(req.getId());
        singer.setName(req.getName());
        singer.setSex(req.getSex());

        // 这里调用 MyBatis-Plus 在 service 层提供的 update 方法，不再直接调用 mapper 层的 update 方法
        return updateById(singer);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean updateBatch(SingerUpdateBatchReq req) throws ServiceException {
        List<Singer> singerList = new ArrayList<>();
        req.getSingerList().forEach(item -> {
            Singer singer = new Singer();
            singer.setId(item.getId());
            singer.setName(item.getName());
            singer.setSex(item.getSex());
            singerList.add(singer);
        });

        // 这里调用 MyBatis-Plus 在 service 层提供的 updateBatch 方法，不再直接调用 mapper 层的 updateBatch 方法
        return updateBatchById(singerList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ListData<Singer> list(SingerListQuery query) throws ServiceException {
        // wrapper 用来添加查询条件
        LambdaQueryWrapper<Singer> wrapper = new LambdaQueryWrapper<>();

        // 排序：先按 name 升序排序，如果 name 相同，再按 id 降序排序
        wrapper.orderByAsc(Singer::getName)
                .orderByDesc(Singer::getId);

        // 有模糊搜索参数
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            // 在姓名和性别字段中模糊搜索
            wrapper.and(w ->
                    w.like(Singer::getName, query.getKeyword())
                            .or()
                            .like(Singer::getSex, query.getKeyword())
            );
        }

        if (query.getPageNum() != null && query.getPageSize() != null) { // 要搞分页，别忘了在 MyBatisPlusConfig 里添加一下 MyBatisPlus 的分页插件
            Page<Singer> page = new Page<>(query.getPageNum(), query.getPageSize());
            // 这里调用 MyBatis-Plus 在 service 层提供的 page 方法，不再直接调用 mapper 层的 selectPage 方法
            Page<Singer> queryedPage = page(page, wrapper);

            // 组装列表查询结果
            return ListData.fromPage(queryedPage);
        } else { // 不搞分页
            // 这里调用 MyBatis-Plus 在 service 层提供的 list 方法，不再直接调用 mapper 层的 selectList 方法
            List<Singer> singerList = list(wrapper);

            // 组装列表查询结果
            return ListData.fromList(singerList);
        }
    }
}