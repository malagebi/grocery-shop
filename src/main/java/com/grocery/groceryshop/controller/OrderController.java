package com.grocery.groceryshop.controller;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.base.req.OrderCreateReq;
import com.grocery.groceryshop.base.req.OrderUpdateReq;
import com.grocery.groceryshop.entity.Order;
import com.grocery.groceryshop.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
public class OrderController {

    @Resource private OrderService orderService;

    @PostMapping
    @ApiOperation("创建订单")
    public CommonResult<Order> create(@Validated @RequestBody OrderCreateReq req) {
        return CommonResult.success(orderService.createOrder(req));
    }

    @PutMapping
    @ApiOperation("修改订单")
    public CommonResult<Order> update(@Validated @RequestBody OrderUpdateReq req) {
        return CommonResult.success(orderService.updateOrder(req));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除订单")
    public CommonResult<Void> delete(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return CommonResult.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("订单详情")
    public CommonResult<Order> get(@PathVariable("id") Long id) {
        return CommonResult.success(orderService.getOrder(id));
    }

    @GetMapping
    @ApiOperation("订单分页查询")
    public CommonResult<CommonPageInfo<Order>> list(
            @ApiParam("页码") @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @ApiParam("用户ID") @RequestParam(value = "userId", required = false) Long userId,
            @ApiParam("订单状态") @RequestParam(value = "status", required = false) Integer status) {
        return CommonResult.success(orderService.listOrder(pageNum, pageSize, userId, status));
    }

    @PostMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public CommonResult<Order> cancel(@PathVariable("id") Long id) {
        return CommonResult.success(orderService.cancelOrder(id));
    }
}
