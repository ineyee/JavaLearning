package com.ineyee.repository;

import com.ineyee.pojo.po.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

// 在 repository 目录下创建一个 XxxRepository 的空接口类即可
// 只要让接口类继承自 MongoRepository，那么该 repository 层就自动拥有了众多接口方法和 repository 实现
// 泛型指定一下对应的 po 类和 id 的数据类型
public interface ProductRepository extends MongoRepository<Product, String> {
    // MongoRepository 已经提供了基本的 CRUD 方法：
    // save(entity)           - 保存或更新
    // saveAll(entities)      - 批量保存
    // findById(id)           - 根据 ID 查询
    // findAll()              - 查询所有
    // deleteById(id)         - 根据 ID 删除
    // delete(entity)         - 删除实体
    // count()                - 统计数量
    // existsById(id)         - 判断是否存在

//    // 自定义查询方法（方法名查询）
//    List<User> findByName(String name);
//    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
//    List<User> findByNameContaining(String keyword);
//
//    // 使用 @Query 注解自定义查询（MongoDB 查询语法）
//    @Query("{ 'age': { $gte: ?0, $lte: ?1 } }")
//    List<User> findByAgeRange(Integer minAge, Integer maxAge);
}
