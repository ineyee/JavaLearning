package com.ineyee.service;

import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.req.*;

public interface ProductService {
    /**
     * 单个查询
     *
     * @param query 请求参数模型
     * @return pojo
     * ① 数据库里有这条数据时，该方法才会正常返回
     * ② 数据库里没有这条数据时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    ProductDetailDto get(ProductGetQuery query) throws ServiceException;

    /**
     * 单个保存
     *
     * @param req 请求参数模型
     * @return id
     * ① 保存成功时，该方法才会正常返回保存成功那条数据的 id，客户端可以根据 id 再手动查询一次数据库拿到完整数据
     * （之所以不在这里直接返回保存成功的那条数据，是因为很可能客户端需要的数据是多表联查后数据，而不仅仅是这一个表的数据
     * 比如 song 保存成功后，返回列表展示时，song 里面还得有歌手的信息，所以不如让客户端走下 get 接口，拿到想要的数据）
     * ② 保存失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    String save(ProductCreateReq req) throws ServiceException;

    /**
     * 单个删除
     *
     * @param req 请求参数模型
     * @return void
     * ① 删除成功时，该方法才会正常结束
     * ② 删除失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    void remove(ProductDeleteReq req) throws ServiceException;

    /**
     * 单个更新
     *
     * @param req 请求参数模型
     * @return void
     * ① 更新成功时，该方法才会正常结束
     * ② 更新失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    void update(ProductUpdateReq req) throws ServiceException;

    // 嵌套的设计师操作
    void addDesigner(DesignerCreateReq req) throws ServiceException;

    void removeDesigner(DesignerRemoveReq req) throws ServiceException;

    void updateDesigner(DesignerUpdateReq req) throws ServiceException;
}