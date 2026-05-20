package com.grocery.groceryshop.service.impl;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CustomerException;
import com.grocery.groceryshop.base.OrderErrorCode;
import com.grocery.groceryshop.base.req.OrderCreateReq;
import com.grocery.groceryshop.base.req.OrderListReq;
import com.grocery.groceryshop.base.req.OrderUpdateReq;
import com.grocery.groceryshop.entity.Order;
import com.grocery.groceryshop.enums.OrderStatus;
import com.grocery.groceryshop.service.OrderService;
import com.grocery.groceryshop.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    /** Mock 的订单存储，替代 OrderMapper */
    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    /** orderNo -> id 二级索引，供支付模块按单号快速查询 */
    private final Map<String, Long> orderNoIndex = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public OrderVO createOrder(OrderCreateReq req) {
        Long id = idGenerator.getAndIncrement();
        Date now = new Date();
        Order order = new Order();
        order.setId(id);
        order.setOrderNo("ORD" + System.currentTimeMillis() + id);
        order.setUserId(req.getUserId());
        order.setCommodityId(req.getCommodityId());
        order.setCommodityName(req.getCommodityName());
        order.setQuantity(req.getQuantity());
        order.setTotalAmount(req.getTotalAmount());
        order.setStatus(OrderStatus.UNPAID.getCode());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderStore.put(id, order);
        orderNoIndex.put(order.getOrderNo(), id);
        log.info("[创建订单] id={}, orderNo={}", id, order.getOrderNo());
        return toVO(order);
    }

    @Override
    public OrderVO updateOrder(OrderUpdateReq req) {
        Order order = orderStore.get(req.getId());
        if (order == null) {
            throw new CustomerException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        OrderStatus current = OrderStatus.fromCode(order.getStatus());
        if (current == OrderStatus.CANCELLED) {
            throw new CustomerException(OrderErrorCode.ORDER_CANCELLED_NOT_MODIFIABLE);
        }
        if (current == OrderStatus.COMPLETED) {
            throw new CustomerException(OrderErrorCode.ORDER_COMPLETED_NOT_MODIFIABLE);
        }
        if (req.getQuantity() != null) {
            order.setQuantity(req.getQuantity());
        }
        if (req.getTotalAmount() != null) {
            order.setTotalAmount(req.getTotalAmount());
        }
        if (req.getStatus() != null) {
            order.setStatus(req.getStatus());
        }
        order.setUpdateTime(new Date());
        log.info("[更新订单] id={}", req.getId());
        return toVO(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order removed = orderStore.remove(id);
        if (removed == null) {
            throw new CustomerException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        orderNoIndex.remove(removed.getOrderNo());
        log.info("[删除订单] id={}", id);
    }

    @Override
    public OrderVO getOrder(Long id) {
        Order order = orderStore.get(id);
        if (order == null) {
            throw new CustomerException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        return toVO(order);
    }

    @Override
    public CommonPageInfo<OrderVO> listOrder(OrderListReq req) {
        int page = req.getPageNum();
        int size = req.getPageSize();
        Long userId = req.getUserId();
        Integer status = req.getStatus();

        List<Order> filtered = orderStore.values().stream()
                .filter(o -> userId == null || Objects.equals(o.getUserId(), userId))
                .filter(o -> status == null || Objects.equals(o.getStatus(), status))
                .sorted(Comparator.comparing(Order::getCreateTime).reversed())
                .collect(Collectors.toList());

        int total = filtered.size();
        int fromIndex = Math.min((page - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<OrderVO> pageList = filtered.subList(fromIndex, toIndex).stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        CommonPageInfo<OrderVO> result = new CommonPageInfo<>();
        result.setPageNum(page);
        result.setPageSize(size);
        result.setTotal(total);
        result.setPages((int) Math.ceil((double) total / size));
        result.setList(pageList);
        return result;
    }

    @Override
    public OrderVO cancelOrder(Long id) {
        Order order = orderStore.get(id);
        if (order == null) {
            throw new CustomerException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        OrderStatus current = OrderStatus.fromCode(order.getStatus());
        if (current == OrderStatus.CANCELLED) {
            throw new CustomerException(OrderErrorCode.ORDER_ALREADY_CANCELLED);
        }
        if (current == OrderStatus.SHIPPED || current == OrderStatus.COMPLETED) {
            throw new CustomerException(OrderErrorCode.ORDER_STATUS_NOT_CANCELLABLE);
        }
        order.setStatus(OrderStatus.CANCELLED.getCode());
        order.setUpdateTime(new Date());
        log.info("[取消订单] id={}", id);
        return toVO(order);
    }

    @Override
    public OrderVO findByOrderNo(String orderNo) {
        Long id = orderNoIndex.get(orderNo);
        if (id == null) return null;
        Order order = orderStore.get(id);
        return order == null ? null : toVO(order);
    }

    @Override
    public void markOrderPaid(String orderNo) {
        Long id = orderNoIndex.get(orderNo);
        if (id == null) return;
        Order order = orderStore.get(id);
        if (order == null) return;
        order.setStatus(OrderStatus.PAID.getCode());
        order.setUpdateTime(new Date());
        log.info("[订单已支付] orderNo={}", orderNo);
    }

    @Override
    public void markOrderRefunded(String orderNo) {
        Long id = orderNoIndex.get(orderNo);
        if (id == null) return;
        Order order = orderStore.get(id);
        if (order == null) return;
        order.setStatus(OrderStatus.REFUNDED.getCode());
        order.setUpdateTime(new Date());
        log.info("[订单已退款] orderNo={}", orderNo);
    }

    private OrderVO toVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusDesc(OrderStatus.descOf(order.getStatus()));
        return vo;
    }
}
