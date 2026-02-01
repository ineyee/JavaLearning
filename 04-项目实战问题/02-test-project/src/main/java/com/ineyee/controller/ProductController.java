package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.*;
import com.ineyee.common.api.ListData;
import com.ineyee.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 表现层之控制器层
// 控制器层（controller）的职责就是直接与客户端打交道，即：
//   * 接收客户端的请求参数，对客户端的请求参数进行基础有效性校验（在表现层进行请求参数的基础有效性校验是为了在请求进入业务逻辑前就拒绝非法格式，避免无效数据渗透到下层，主要包含：必填字段校验、字段长度校验、字段格式校验等，其它跟业务相关的校验就交给业务层去校验）
//   * 调用业务层的 API 拿到数据并转换成客户端直接能用的 JSON、HTML 等数据，给客户端返回响应
// 换句话说：
//   * 对下，表现层要对业务层负责，负责的体现就是要做好请求参数的基础有效性校验，确保传递给业务层的参数是人家直接能用的，业务层只需要在此基础上附加一层业务规则校验就可以了
//   * 对上，表现层要对客户端负责，负责的体现就是要做好数据转换，确保返回给客户端的数据是人家直接能用的 JSON、HTML 等数据或错误信息
//   * 其它的表现层就不用关心了
// 实践经验：
//   * 接口到底是请求成功还是请求失败，统一在表现层处理，失败了就给客户端响应错误，成功了就给客户端响应数据
//   * 基础有效性校验出错时，需要直接给客户端响应错误
//   * 调用业务层的 API 时一定要用 try-catch，因为数据层和业务层的异常它们都没处理、都是继续上抛到表现层来统一处理的，catch 到异常时就给客户端响应错误，没有错误时就给客户端响应数据
// 但是因为现在有了全局异常处理来给客户端响应错误，所以：
//   * controller 层调用 service 层的 API 时直接调用就可以了，不用再 try-catch（由数据层抛上来的系统异常 + 由业务层抛上来的业务异常），因为 controller 层把异常继续往上抛，就会抛给 DispatcherServlet，DispatcherServlet 捕获到异常就会调用我们定义的异常处理器来统一处理异常、给客户端响应错误
//   * 那 controller 层本身可能产生的异常呢？比如请求路径不存在、请求参数校验出错等，其实这都是系统异常
//       * 请求路径不存在的话，也是系统异常，会被统一拦截掉，统一返回 -100000 那个错误（如果不拦截的话，服务器会自动返回 404、因为这是客户端搞错路径了）
//       * 请求参数校验出错的话，也是系统异常，会被统一拦截掉，统一返回 -100000 那个错误（如果不拦截的话，服务器会自动返回  400、因为这是客户端没按服务端的要求传递参数）
//   * 因此更进一步 controller 里只写成功时响应数据的代码即可、不需要写失败时响应错误的代码，因为整个过程中出现的任何异常都会被异常处理器拦截掉来统一响应错误给客户端
@Slf4j
@RestController
@RequestMapping("/product")
@Tag(name = "产品模块")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    @Operation(summary = "获取产品详情")
    public HttpResult<ProductDetailDto> get(@Valid ProductGetQuery query) throws ServiceException {
        ProductDetailDto data = productService.get(query);
        return HttpResult.ok(data);
    }

    @GetMapping("/list")
    @Operation(summary = "获取产品列表")
    public HttpResult<ListData<ProductDetailDto>> list(@Valid ProductListQuery query) {
        ListData<ProductDetailDto> dataList = productService.list(query);
        return HttpResult.ok(dataList);
    }

    @PostMapping("/save")
    @Operation(summary = "保存产品")
    public HttpResult<ProductDetailDto> save(@Valid @RequestBody ProductCreateReq req) throws ServiceException {
        ProductDetailDto data = productService.save(req);
        return HttpResult.ok(data);
    }

    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存产品")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody ProductCreateBatchReq req) throws ServiceException {
        List<Long> idList = productService.saveBatch(req);
        return HttpResult.ok(idList);
    }

    @PostMapping("/remove")
    @Operation(summary = "删除产品")
    public HttpResult<Void> remove(@Valid @RequestBody ProductDeleteReq req) throws ServiceException {
        productService.remove(req);
        return HttpResult.ok();
    }

    @PostMapping("/removeBatch")
    @Operation(summary = "批量删除产品")
    public HttpResult<Void> removeBatch(@Valid @RequestBody ProductDeleteBatchReq req) throws ServiceException {
        productService.removeBatch(req);
        return HttpResult.ok();
    }

    @PostMapping("/update")
    @Operation(summary = "更新产品")
    public HttpResult<Void> update(@Valid @RequestBody ProductUpdateReq req) throws ServiceException {
        productService.update(req);
        return HttpResult.ok();
    }

    @PostMapping("/updateBatch")
    @Operation(summary = "批量更新产品")
    public HttpResult<Void> updateBatch(@Valid @RequestBody ProductUpdateBatchReq req) throws ServiceException {
        productService.updateBatch(req);
        return HttpResult.ok();
    }
}
