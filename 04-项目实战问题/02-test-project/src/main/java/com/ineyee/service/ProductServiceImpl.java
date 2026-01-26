package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.mapper.ProductMapper;
import com.ineyee.pojo.po.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 在 service 目录下创建一个 XxxServiceImpl 的空实现类即可
// 只要让实现类继承自 ServiceImpl，那么该实现类就自动拥有了众多接口方法的实现
// 泛型指定一下对应的 mapper 类 和 po 类
@Service
@Transactional
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
