package com.grocery.groceryshop.disruptor.handler;

import com.grocery.groceryshop.disruptor.event.OrderEvent;
import com.grocery.groceryshop.disruptor.event.OrderEventType;
import com.lmax.disruptor.EventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 第二级消费者（与 NotifyHandler 并行）：按事件类型累计计数。
 * 单线程消费，AtomicLong 仅供外部线程安全读取。
 */
@Getter
@Slf4j
@Component
public class OrderStatHandler implements EventHandler<OrderEvent> {

    private final Map<OrderEventType, AtomicLong> counters = new EnumMap<>(OrderEventType.class);

    public OrderStatHandler() {
        for (OrderEventType type : OrderEventType.values()) {
            counters.put(type, new AtomicLong(0));
        }
    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        long count = counters.get(event.getEventType()).incrementAndGet();
        log.info("[统计] action={} 累计={}", event.getEventType().getDesc(), count);
    }

}
