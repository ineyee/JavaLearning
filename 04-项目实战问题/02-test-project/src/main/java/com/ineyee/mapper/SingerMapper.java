package com.ineyee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ineyee.pojo.po.Singer;

// 在 mapper 目录下创建一个 XxxMapper 的空接口类即可
// 需要让我们的接口类继承自 MyBatis-Plus 的 BaseMapper 接口，这样一来当前 mapper 层就自动拥有了众多接口方法和 mapper 实现
// 泛型需要指定一下对应的 po 类
public interface SingerMapper extends BaseMapper<Singer> {
}
