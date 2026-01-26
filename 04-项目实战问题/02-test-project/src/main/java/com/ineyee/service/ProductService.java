package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ineyee.pojo.po.Product;

// 在 service 目录下创建一个 XxxService 的空接口类即可
// 只要让接口类继承自 IService，那么该接口类就自动拥有了众多接口方法
// 泛型指定一下对应的 po 类
public interface ProductService extends IService<Product> {

}
