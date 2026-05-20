package com.grocery.groceryshop.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    UNPAID(1, "待支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消"),
    REFUNDED(6, "已退款");

    private final Integer code;
    private final String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatus s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }
        return null;
    }

    public static String descOf(Integer code) {
        OrderStatus s = fromCode(code);
        return s == null ? null : s.desc;
    }
}
