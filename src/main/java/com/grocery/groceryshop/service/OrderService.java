package com.grocery.groceryshop.service;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.req.OrderCreateReq;
import com.grocery.groceryshop.base.req.OrderListReq;
import com.grocery.groceryshop.base.req.OrderUpdateReq;
import com.grocery.groceryshop.vo.OrderVO;

public interface OrderService {

    OrderVO createOrder(OrderCreateReq req);

    OrderVO updateOrder(OrderUpdateReq req);

    void deleteOrder(Long id);

    OrderVO getOrder(Long id);

    CommonPageInfo<OrderVO> listOrder(OrderListReq req);

    OrderVO cancelOrder(Long id);
}
