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

    /** 按订单号查询订单，支付模块内部使用，找不到时返回 null */
    OrderVO findByOrderNo(String orderNo);

    /** 将订单标记为已支付，由支付回调触发 */
    void markOrderPaid(String orderNo);

    /** 将订单标记为已退款，由退款操作触发 */
    void markOrderRefunded(String orderNo);
}
