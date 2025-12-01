package com.ineyee._04_aop.basicuse.servlet;

import com.ineyee._04_aop.basicuse.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderServlet {
    // 控制器层表面还是持有业务层，实际却是持有业务层代理（本质原因是我们在 afterInit 生命周期方法里把创建业务层对象“篡改”成了创建业务层代理对象）
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public Boolean pay() {
        // 控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码（相当于租客跟中介对接）
        return orderService.pay();
    }

    public void view() {
        // 控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码（相当于租客跟中介对接）
        orderService.view();
    }
}