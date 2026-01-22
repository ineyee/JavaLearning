package com.ineyee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.SingerMapper;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.SingerCreateBatchReq;
import com.ineyee.pojo.req.SingerCreateReq;
import com.ineyee.pojo.req.SingerUpdateBatchReq;
import com.ineyee.pojo.req.SingerUpdateReq;
import com.ineyee.pojo.vo.ListData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

// 1、在 service 目录下创建一个 XxxServiceImpl 的空实现类即可
// 需要让我们的接口类继承自 MyBatis-Plus 的 ServiceImpl 接口，这样一来当前实现类就自动拥有了众多接口方法的实现
// 泛型需要指定一下对应的 mapper 类 和 po 类
//
// 2、用户模块接口的业务层
// 业务层（service）的职责就是处理我们实际的业务逻辑，如业务规则校验、数据库事务管理等
// 业务层内部在调用数据层的 API（现在改成了调用 MyBatis-Plus 在 service 层提供的 save 方法），等待数据层给它返回“影响的数据条数”或“原始模型 po”，然后把它们处理成表现层更方便使用的业务型数据交付给表现层，比如“影响的数据条数”转换成“成功与否”、“完整 po 数据”这样更易使用的业务型数据
//
// 换句话说：
//   * 对下，业务层要对数据层负责，负责的体现就是要做好业务规则校验，确保传递给数据层的参数是人家直接能用的，不用人家再做什么乱七八糟的校验
//   * 对上
//     * 业务层要绝对相信表现层，相信的体现就是直接拿着参数去做业务，而不再纠结于参数会不会出问题，要绝对相信经过表现层校验后的参数是肯定没问题的
//     * 业务层要对表现层负责，负责的体现就是要做好数据转换，把数据处理成表现层更方便使用的业务型数据交付给表现层
//   * 其它的业务层就不用关心了，至于执行结果失败了怎样、成功了怎样，那是表现层拿到结果后该干的事
// 实践经验：
//  * 执行成功时，总是把执行成功的结果返回，表现层用不用执行成功的结果由它自己决定
//  * 执行失败时，这里不需要 try-catch 数据层 API 执行失败的异常，继续往上层抛、抛到表现层
//  * 业务规则校验出错或做业务出错时，需要我们主动抛出异常、抛到表现层
//
// 注意：业务层绝对不是数据层的马甲层，它有存在的意义，比如同样都是 save 方法，数据层里的 save 就是直接往数据库里写数据
// 而业务层里的 save 则需要首先判断数据库里存不存在这个用户邮箱，存在的话就跳过，不存在的话再往数据库里写，因为用户邮箱是不允许重复的
// 也就是说，业务层的一个方法里可能会调用数据层里的多个方法来共同完成一个业务
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
    public ListData<Singer> list(SingerListQuery query) throws ServiceException {
        // wrapper 用来添加查询条件
        LambdaQueryWrapper<Singer> wrapper = new LambdaQueryWrapper<>();

        // 排序：先按 name 升序排序，如果 name 相同，再按 id 降序排序
        wrapper.orderByAsc(Singer::getName)
                .orderByDesc(Singer::getId);

        // 有模糊搜索参数
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            // 在姓名和性别字段中模糊搜索
            wrapper.like(Singer::getName, query.getKeyword())
                    .or()
                    .like(Singer::getSex, query.getKeyword());
        }

        if (query.getPageNum() != null && query.getPageSize() != null) { // 要搞分页，别忘了在 MyBatisPlusConfig 里添加一下 MyBatis-Plus 的分页插件
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