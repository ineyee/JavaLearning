package com.ineyee.service;

import com.ineyee.common.api.ListData;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.dto.ProductListDto;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.*;

import java.util.List;

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
}