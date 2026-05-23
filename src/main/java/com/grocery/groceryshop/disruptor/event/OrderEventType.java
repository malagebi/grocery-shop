package com.grocery.groceryshop.disruptor.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderEventType {
    CREATE("下单"),
    PAY("支付"),
    CANCEL("取消"),
    COMPLETE("完成"),
    REFUND("退款");

    private final String desc;
}
