package com.grocery.groceryshop.disruptor.event;

import lombok.Data;

import java.math.BigDecimal;

/**
 * RingBuffer 中预分配的事件槽，所有字段必须可变以便复用。
 * 生产者每次发布前调用 setter 覆盖旧数据，消费者只读。
 */
@Data
public class OrderEvent {

    private String orderNo;
    private Long userId;
    private OrderEventType eventType;
    private BigDecimal amount;
    private long timestamp;

    public void reset() {
        this.orderNo = null;
        this.userId = null;
        this.eventType = null;
        this.amount = null;
        this.timestamp = 0L;
    }
}
