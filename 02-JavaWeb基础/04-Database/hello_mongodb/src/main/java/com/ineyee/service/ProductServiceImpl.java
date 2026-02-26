package com.ineyee.service;

import com.ineyee.common.api.error.ProductServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.po.Designer;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.req.*;
import com.ineyee.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository; // 用来执行【增删改 + 基本查】等简单操作

    // MongoTemplate：spring-boot-starter-data-mongodb 的核心操作类，提供对 MongoDB 的底层操作能力
    @Autowired
    private MongoTemplate mongoTemplate; // 用来执行【数组类型字段的元素操作 + 】等复杂操作

    @Override
    public ProductDetailDto get(ProductGetQuery query) throws ServiceException {
        // 这里调用 MongoDB 在 repository 层提供的 findById 方法
        Optional<Product> optionalProduct = productRepository.findById(query.getId());
        // 查询出来时，手动判断下是否软删除
        if (optionalProduct.isEmpty() || optionalProduct.get().getDeleted() == 1) {
            // 如果数据库执行 MQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表数据库里肯定没有这条数据，抛个业务异常
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }

        return ProductDetailDto.from(optionalProduct.get());
    }

    @Override
    public String save(ProductCreateReq req) throws ServiceException {
        Product product = new Product();
        product.setName(req.getName());
        product.setDesc(req.getDesc());
        product.setPrice(req.getPrice());
        product.setDesignerList(req.getDesignerList());
        // 新增数据时，手动设置下软删除字段的初始值
        product.setDeleted(0);

        // 这里调用 MongoDB 在 repository 层提供的 insert 方法
        Product savedProduct = productRepository.insert(product);
        return savedProduct.getId();
    }

    // 这里是软删除，所以本质上是一个手动更新 deleted 字段的操作
    @Override
    public void remove(ProductDeleteReq req) throws ServiceException {
        // 先看看对应的数据存不存在
        Optional<Product> optionalProduct = productRepository.findById(req.getId());
        if (optionalProduct.isEmpty() || optionalProduct.get().getDeleted() == 1) {
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }

        // 存在的话，更新下 deleted 字段
        Product product = optionalProduct.get();
        product.setDeleted(1);
        // 重新把数据保存到数据库，只会更新 deleted 字段
        productRepository.save(product);
    }

    @Override
    public void update(ProductUpdateReq req) throws ServiceException {
        // 先看看对应的数据存不存在
        Optional<Product> optionalProduct = productRepository.findById(req.getId());
        if (optionalProduct.isEmpty() || optionalProduct.get().getDeleted() == 1) {
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }

        // 存在的话，更新下客户端传过来的字段，所以这里要判断掉 ProductUpdateReq 里的所有可更新字段
        Product product = optionalProduct.get();
        product.setId(req.getId());
        if (req.getName() != null) product.setName(req.getName());
        if (req.getDesc() != null) product.setDesc(req.getDesc());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        // 这种更新是整个数组覆盖式的更新，要想精确操作数组中的某个元素，需要使用 MongoTemplate 而非 repository
        if (req.getDesignerList() != null) product.setDesignerList(req.getDesignerList());
        // 重新把数据保存到数据库，只会更新客户端传过来的字段
        productRepository.save(product);
    }

    // ========== 以下是使用 MongoTemplate 精确操作数组元素的方法（增删改数组里的元素对于产品来说都是更新它的数组字段） ==========

    /**
     * 场景 1：向数组添加元素
     * 例如：添加一个新设计师
     */
    public void addDesigner(DesignerCreateReq req) {
        String productId = req.getProductId();
        Designer designer = new Designer();
        designer.setName(req.getName());
        designer.setAge(req.getAge());
        designer.setSex(req.getSex());

        // ObjectId：MongoDB 的主键类型，将字符串形式的 ID 转换为 MongoDB 的 ObjectId 类型
        // Criteria（条件）：构建具体的查询条件，支持链式 API 来构建复杂的查询条件
        // - Criteria.where("_id").is(new ObjectId(productId))：指定查询条件为 _id 等于 productId
        // Query：封装查询条件为查询对象，类似 SQL 的 WHERE 子句
        Query query = new Query(Criteria.where("_id").is(new ObjectId(productId)).and("deleted").is(0));

        // Update：构建更新规则，类似于 SQL 的 SET 子句
        // - new Update()：创建一个更新对象
        // - addToSet("designerList", newDesigner)：以 addToSet 的方式往 designerList 数组里添加一个元素 designer，也可以使用 push
        Update update = new Update().addToSet("designerList", designer);

        // 执行更新操作
        // updateFirst：代表只更新第一条匹配到的数据（即产品），如果要更新所有匹配的数据可以使用 updateMulti
        // query：按照 query 对象指定的查询条件，找到要更新的那条数据
        // update：按照 update 对象指定的更新规则，更新数据
        // Product.class：告诉 MongoDB 要操作哪个集合
        mongoTemplate.updateFirst(query, update, Product.class);

        // 等价的 MongoDB 更新语句：
        // db.product.updateOne(
        //   { "_id": ObjectId("699e6aac131e0d5a83054acf") },
        //   { "$addToSet": { "designerList": { "name": "苹果最牛新设计师", "age": 30, "sex": "男" } } }
        // )
    }

    /**
     * 场景 2：从数组删除元素
     * 例如：删除名字为"${designerName}"的设计师（实际开发中可根据 designerId 来删，我们这里的 demo 里没有 id 字段，所以就用 name 了）
     */
    public void removeDesigner(DesignerRemoveReq req) throws ServiceException {
        String productId = req.getProductId();
        String designerName = req.getDesignerName();

        // 找到要更新的那条数据
        Query query = new Query(Criteria.where("_id").is(new ObjectId(productId)).and("deleted").is(0));
        // 从数组里删除名字为"$designerName"的元素
        Update update = new Update().pull("designerList", Query.query(Criteria.where("name").is(designerName)));
        // 执行更新操作
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        // 没有找到任何一条数据，说明产品不存在
        if (result.getMatchedCount() == 0) {
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }
        // 产品存在，但是没有修改任何一条数据，说明设计师不在数组中
        if (result.getModifiedCount() == 0) {
            throw new ServiceException(ProductServiceError.DESIGNER_NOT_EXIST);
        }
    }

    /**
     * 场景 3：更新数组中符合条件的元素
     * 例如：更新名字为"${designerName}"的设计师的年龄（实际开发中可根据 designerId 来更新，我们这里的 demo 里没有 id 字段，所以就用 name 了）
     * 注意：$ 操作符只会更新第一个匹配的元素
     */
    public void updateDesigner(DesignerUpdateReq req) throws ServiceException {
        String productId = req.getProductId();
        String designerName = req.getDesignerName();
        Designer designer = new Designer();
        if (req.getAge() != null) designer.setAge(req.getAge());
        if (req.getSex() != null) designer.setSex(req.getSex());

        // 找到要更新的那条数据
        Query query = new Query(Criteria.where("_id").is(new ObjectId(productId)).and("deleted").is(0)
                .and("designerList.name").is(designerName));
        // 更新数组中符合条件的元素
        // 更新下客户端传过来的字段，所以这里要判断掉 Designer 里的所有可更新字段
        Update update = new Update();
        if (designer.getAge() != null) {
            update.set("designerList.$.age", designer.getAge());
        }
        if (designer.getSex() != null) {
            update.set("designerList.$.sex", designer.getSex());
        }
        if (update.getUpdateObject().isEmpty()) {
            // 没有字段需要更新
            return;
        }
        // 执行更新操作
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        // 没有找到任何一条数据，说明产品不存在
        if (result.getMatchedCount() == 0) {
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }
        // 产品存在，但是没有修改任何一条数据，说明设计师不在数组中
        if (result.getModifiedCount() == 0) {
            throw new ServiceException(ProductServiceError.DESIGNER_NOT_EXIST);
        }
    }
}
