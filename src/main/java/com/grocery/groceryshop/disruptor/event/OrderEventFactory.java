package com.grocery.groceryshop.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * Disruptor 启动时调用此工厂预填充整个 RingBuffer，避免运行期 GC 压力。
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
