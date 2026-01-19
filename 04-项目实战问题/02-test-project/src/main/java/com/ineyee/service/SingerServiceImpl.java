package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.ineyee.mapper.SingerMapper;
import com.ineyee.pojo.po.Singer;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 在 service 目录下创建一个 XxxServiceImpl 的空实现类即可
// 需要让我们的接口类继承自 MyBatis-Plus 的 ServiceImpl 接口，这样一来当前实现类就自动拥有了众多接口方法的实现
// 泛型需要指定一下对应的 mapper 类 和 po 类
@Service
@Transactional
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {
}

// 出于演示简单起见，这个业务层就不做业务规则校验了，直接调用 dao 的 API
//
// 用户模块接口的业务层
//
// 业务层（service）的职责就是处理我们实际的业务逻辑，如业务规则校验、数据库事务管理等
// 业务层内部在调用数据层的 API，等待数据层给它返回“影响的数据条数”或“原始模型 domain”，然后把它们处理成表现层更方便使用的业务型数据交付给表现层，比如“影响的数据条数”转换成“成功与否”、“完整 domain 数据”这样更易使用的业务型数据
//
// 换句话说：
//   * 对下，业务层要对数据层负责，负责的体现就是要做好业务规则校验，确保传递给数据层的参数是人家直接能用的，不用人家再做什么乱七八糟的校验
//   * 对上
//     * 业务层要绝对相信表现层，相信的体现就是直接拿着参数去做业务，而不再纠结于参数会不会出问题，要绝对相信经过表现层校验后的参数是肯定没问题的
//     * 业务层要对表现层负责，负责的体现就是要做好数据转换，把数据处理成表现层更方便使用的业务型数据交付给表现层
//   * 其它的业务层就不用关心了，至于执行结果失败了怎样、成功了怎样，那是表现层拿到结果后该干的事
//
// 实践经验：
//  * 执行成功时，总是把执行成功的结果返回，表现层用不用执行成功的结果由它自己决定
//  * 执行失败时，这里不需要 try-catch 数据层 API 执行失败的异常，继续往上层抛、抛到表现层
//  * 业务规则校验出错或做业务出错时，需要我们主动抛出异常、抛到表现层
//
// 注意：业务层绝对不是数据层的马甲层，它有存在的意义，比如同样都是 save 方法，数据层里的 save 就是直接往数据库里写数据
// 而业务层里的 save 则需要首先判断数据库里存不存在这个用户邮箱，存在的话就跳过，不存在的话再往数据库里写，因为用户邮箱是不允许重复的
// 也就是说，业务层的一个方法里可能会调用数据层里的多个方法来共同完成一个业务
//@Service
//@Transactional
//public class SingerServiceImpl implements SingerService {
//    @Autowired
//    private SingerMapper singerMapper;
//
//    @Override
//    @Transactional(propagation = Propagation.SUPPORTS)
//    public Singer get(Long id) {
//        return singerMapper.selectById(id);
//    }
//
/// /    @Override
/// /    @Transactional(propagation = Propagation.SUPPORTS)
/// /    public List<Singer> list(Integer pageSize, Integer pageNum) {
/// /        return singerMapper.selectPage(Map.of(
/// /                "limit", pageSize,
/// /                "offset", (pageNum - 1) * pageSize
/// /        ));
/// /    }
/// /
/// /    @Override
/// /    public List<Singer> listPageHelper(Integer pageSize, Integer pageNum) {
/// /        PageHelper.startPage(pageNum, pageSize);
/// /        return singerMapper.listPageHelper();
/// /    }
//
//    @Override
//    public Singer save(Singer singer) {
//        int ret = singerMapper.insert(singer);
//        return ret > 0 ? singer : null;
//    }
//
//    @Override
//    public List<Long> saveBatch(List<Singer> singerList) {
//        List<BatchResult> ret = singerMapper.insert(singerList);
//        return !ret.isEmpty() ? singerList.stream().map(Singer::getId).toList() : new ArrayList<>();
//    }
//
//    @Override
//    public Boolean remove(Long id) {
//        int ret = singerMapper.deleteById(id);
//        return ret > 0;
//    }
//
//    @Override
//    public Boolean removeBatch(List<Long> idList) {
//        int ret = singerMapper.deleteByIds(idList);
//        return ret > 0;
//    }
//
//    @Override
//    public Boolean update(Singer singer) {
//        return singerMapper.insertOrUpdate(singer);
//    }
//
//    @Override
//    public Boolean updateBatch(List<Singer> singerList) {
//        List<BatchResult> ret = singerMapper.insertOrUpdate(singerList);
//        return !ret.isEmpty();
//    }
//}