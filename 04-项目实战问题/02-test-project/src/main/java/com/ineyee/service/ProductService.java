package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ineyee.pojo.po.Product;

// 1、在 service 目录下创建一个 XxxService 的空接口类即可
// 只要让接口类继承自 IService，那么该接口类就自动拥有了众多接口方法
// 泛型指定一下对应的 po 类
//
// 2、一张表对应的 service，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据层的方案，这种面向接口编程的方式方便切换方案
public interface ProductService extends IService<Product> {

}
