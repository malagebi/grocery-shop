package com.grocery.groceryshop.disruptor.handler;

import com.grocery.groceryshop.disruptor.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 第一级消费者：审计日志。
 * StatHandler 和 NotifyHandler 在此之后并行执行（由 DSL .then() 保证顺序）。
 */
@Slf4j
@Component
public class OrderAuditLogHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        log.info("[审计日志] seq={} orderNo={} userId={} action={} amount={} ts={}",
                sequence,
                event.getOrderNo(),
                event.getUserId(),
                event.getEventType().getDesc(),
                event.getAmount(),
                event.getTimestamp());
    }
}
