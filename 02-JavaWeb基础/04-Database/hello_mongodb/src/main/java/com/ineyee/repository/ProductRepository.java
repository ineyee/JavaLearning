package com.ineyee.repository;

import com.ineyee.pojo.po.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

// 在 repository 目录下创建一个 XxxRepository 的空接口类即可
// 只要让接口类继承自 MongoRepository，那么该 repository 层就自动拥有了众多接口方法和 repository 实现
// 泛型指定一下对应的 po 类和 id 的数据类型
public interface ProductRepository extends MongoRepository<Product, String> {

}
