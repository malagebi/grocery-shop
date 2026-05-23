package com.grocery.groceryshop.disruptor.handler;

import com.grocery.groceryshop.disruptor.event.OrderEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 第二级消费者（与 StatHandler 并行）：模拟推送通知给用户。
 * 实际场景可替换为 WebSocket push / 短信 / App 推送。
 */
@Slf4j
@Component
public class OrderNotifyHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        String message = buildMessage(event);
        log.info("[推送通知] userId={} 消息：{}", event.getUserId(), message);
        // 实际项目此处调用 WebSocket / 消息服务
    }

    private String buildMessage(OrderEvent event) {
        switch (event.getEventType()) {
            case CREATE:  return "您的订单 " + event.getOrderNo() + " 已创建，请尽快完成支付";
            case PAY:     return "订单 " + event.getOrderNo() + " 支付成功，金额 " + event.getAmount();
            case CANCEL:  return "订单 " + event.getOrderNo() + " 已取消";
            case COMPLETE: return "订单 " + event.getOrderNo() + " 已完成，感谢购买！";
            case REFUND:  return "订单 " + event.getOrderNo() + " 退款已受理，金额 " + event.getAmount();
            default:      return "订单 " + event.getOrderNo() + " 状态已更新";
        }
    }
}
