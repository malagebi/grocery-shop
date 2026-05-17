package com.grocery.groceryshop.service;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.req.OrderCreateReq;
import com.grocery.groceryshop.base.req.OrderUpdateReq;
import com.grocery.groceryshop.entity.Order;

public interface OrderService {

    Order createOrder(OrderCreateReq req);

    Order updateOrder(OrderUpdateReq req);

    void deleteOrder(Long id);

    Order getOrder(Long id);

    CommonPageInfo<Order> listOrder(Integer pageNum, Integer pageSize, Long userId, Integer status);

    Order cancelOrder(Long id);
}
