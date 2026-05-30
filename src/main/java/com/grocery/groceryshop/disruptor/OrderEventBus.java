package com.grocery.groceryshop.disruptor;

import com.grocery.groceryshop.disruptor.event.OrderEvent;
import com.grocery.groceryshop.disruptor.event.OrderEventFactory;
import com.grocery.groceryshop.disruptor.event.OrderEventType;
import com.grocery.groceryshop.disruptor.handler.OrderAuditLogHandler;
import com.grocery.groceryshop.disruptor.handler.OrderNotifyHandler;
import com.grocery.groceryshop.disruptor.handler.OrderStatHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.math.BigDecimal;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单事件总线，封装 Disruptor 生命周期与发布接口。
 *
 * 消费拓扑：
 *   AuditLogHandler（第一级，串行）
 *       └─► StatHandler    ┐（第二级，并行）
 *       └─► NotifyHandler  ┘
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventBus {

    /** RingBuffer 大小，必须是 2 的幂 */
    private static final int RING_BUFFER_SIZE = 1024;

    private final OrderAuditLogHandler auditLogHandler;
    @Getter
    private final OrderStatHandler statHandler;
    private final OrderNotifyHandler notifyHandler;

    private Disruptor<OrderEvent> disruptor;
    private RingBuffer<OrderEvent> ringBuffer;

    @PostConstruct
    public void init() {
        AtomicInteger idx = new AtomicInteger(1);
        ThreadFactory threadFactory = r -> new Thread(r, "order-event-worker-" + idx.getAndIncrement());

        disruptor = new Disruptor<>(
                new OrderEventFactory(),
                RING_BUFFER_SIZE,
                threadFactory,
                ProducerType.MULTI,         // 允许多个生产者线程并发发布
                new BlockingWaitStrategy()  // 低延迟要求不高时首选，CPU 友好
        );

        // 声明消费拓扑：AuditLog 先执行，之后 Stat 与 Notify 并行
        disruptor.handleEventsWith(auditLogHandler)
                 .then(statHandler, notifyHandler);

        ringBuffer = disruptor.start();
        log.info("[OrderEventBus] Disruptor 已启动，RingBuffer 容量={}", RING_BUFFER_SIZE);
    }

    @PreDestroy
    public void destroy() {
        if (disruptor != null) {
            disruptor.shutdown();
            log.info("[OrderEventBus] Disruptor 已关闭");
        }
    }

    /**
     * 发布订单事件。
     * 使用 try/finally 保证即便填充数据异常也会 publish，避免消费者永久阻塞。
     */
    public void publish(String orderNo, Long userId, OrderEventType eventType, BigDecimal amount) {
        long sequence = ringBuffer.next();
        try {
            OrderEvent event = ringBuffer.get(sequence);
            event.setOrderNo(orderNo);
            event.setUserId(userId);
            event.setEventType(eventType);
            event.setAmount(amount);
            event.setTimestamp(System.currentTimeMillis());
        } finally {
            ringBuffer.publish(sequence);
        }
        log.debug("[OrderEventBus] 已发布 seq={} orderNo={} action={}", sequence, orderNo, eventType);
    }

}
