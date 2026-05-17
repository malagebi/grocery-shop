package com.grocery.groceryshop.service.impl;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CustomerException;
import com.grocery.groceryshop.base.req.OrderCreateReq;
import com.grocery.groceryshop.base.req.OrderUpdateReq;
import com.grocery.groceryshop.entity.Order;
import com.grocery.groceryshop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
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

    private static final Integer STATUS_UNPAID = 1;
    private static final Integer STATUS_PAID = 2;
    private static final Integer STATUS_SHIPPED = 3;
    private static final Integer STATUS_COMPLETED = 4;
    private static final Integer STATUS_CANCELLED = 5;

    /** Mock 的订单存储，替代 OrderMapper */
    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Order createOrder(OrderCreateReq req) {
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
        order.setStatus(STATUS_UNPAID);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderStore.put(id, order);
        log.info("[创建订单] id={}, orderNo={}", id, order.getOrderNo());
        return order;
    }

    @Override
    public Order updateOrder(OrderUpdateReq req) {
        Order order = orderStore.get(req.getId());
        if (order == null) {
            throw new CustomerException("404", "订单不存在");
        }
        if (Objects.equals(order.getStatus(), STATUS_CANCELLED)) {
            throw new CustomerException("400", "已取消的订单不可修改");
        }
        if (Objects.equals(order.getStatus(), STATUS_COMPLETED)) {
            throw new CustomerException("400", "已完成的订单不可修改");
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
        return order;
    }

    @Override
    public void deleteOrder(Long id) {
        if (orderStore.remove(id) == null) {
            throw new CustomerException("404", "订单不存在");
        }
        log.info("[删除订单] id={}", id);
    }

    @Override
    public Order getOrder(Long id) {
        Order order = orderStore.get(id);
        if (order == null) {
            throw new CustomerException("404", "订单不存在");
        }
        return order;
    }

    @Override
    public CommonPageInfo<Order> listOrder(Integer pageNum, Integer pageSize, Long userId, Integer status) {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;

        List<Order> filtered = orderStore.values().stream()
                .filter(o -> userId == null || Objects.equals(o.getUserId(), userId))
                .filter(o -> status == null || Objects.equals(o.getStatus(), status))
                .sorted(Comparator.comparing(Order::getCreateTime).reversed())
                .collect(Collectors.toList());

        int total = filtered.size();
        int fromIndex = Math.min((page - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<Order> pageList = filtered.subList(fromIndex, toIndex);

        CommonPageInfo<Order> result = new CommonPageInfo<>();
        result.setPageNum(page);
        result.setPageSize(size);
        result.setTotal(total);
        result.setPages((int) Math.ceil((double) total / size));
        result.setList(pageList);
        return result;
    }

    @Override
    public Order cancelOrder(Long id) {
        Order order = orderStore.get(id);
        if (order == null) {
            throw new CustomerException("404", "订单不存在");
        }
        if (Objects.equals(order.getStatus(), STATUS_CANCELLED)) {
            throw new CustomerException("400", "订单已取消，请勿重复操作");
        }
        if (Objects.equals(order.getStatus(), STATUS_SHIPPED)
                || Objects.equals(order.getStatus(), STATUS_COMPLETED)) {
            throw new CustomerException("400", "当前订单状态不允许取消");
        }
        order.setStatus(STATUS_CANCELLED);
        order.setUpdateTime(new Date());
        log.info("[取消订单] id={}", id);
        return order;
    }
}
