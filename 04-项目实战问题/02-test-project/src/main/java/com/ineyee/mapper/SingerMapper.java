package com.ineyee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ineyee.pojo.po.Singer;

// 在 mapper 目录下创建一个 XxxMapper 的空接口类即可
// 只要让接口类继承自 BaseMapper，那么该 mapper 层就自动实现了众多接口方法和 mapper 实现
// 泛型指定一下对应的 po 类
public interface SingerMapper extends BaseMapper<Singer> {
}
