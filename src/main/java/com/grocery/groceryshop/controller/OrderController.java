package com.grocery.groceryshop.controller;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.base.req.OrderCreateReq;
import com.grocery.groceryshop.base.req.OrderListReq;
import com.grocery.groceryshop.base.req.OrderUpdateReq;
import com.grocery.groceryshop.service.OrderService;
import com.grocery.groceryshop.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/order")
@Tag(name = "订单模块")
public class OrderController {

    @Resource private OrderService orderService;

    @PostMapping
    @Operation(summary ="创建订单")
    public CommonResult<OrderVO> create(@Validated @RequestBody OrderCreateReq req) {
        return CommonResult.success(orderService.createOrder(req));
    }

    @PutMapping
    @Operation(summary ="修改订单")
    public CommonResult<OrderVO> update(@Validated @RequestBody OrderUpdateReq req) {
        return CommonResult.success(orderService.updateOrder(req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary ="删除订单")
    public CommonResult<Void> delete(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return CommonResult.success();
    }

    @GetMapping("/{id}")
    @Operation(summary ="订单详情")
    public CommonResult<OrderVO> get(@PathVariable("id") Long id) {
        return CommonResult.success(orderService.getOrder(id));
    }

    @GetMapping("/list")
    @Operation(summary ="订单分页查询")
    public CommonResult<CommonPageInfo<OrderVO>> list(OrderListReq req) {
        return CommonResult.success(orderService.listOrder(req));
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary ="取消订单")
    public CommonResult<OrderVO> cancel(@PathVariable("id") Long id) {
        return CommonResult.success(orderService.cancelOrder(id));
    }
}
