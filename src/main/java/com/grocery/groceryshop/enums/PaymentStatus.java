package com.grocery.groceryshop.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PROCESSING(1, "处理中"),
    SUCCESS(2, "支付成功"),
    FAILED(3, "支付失败"),
    REFUNDED(4, "已退款");

    private final Integer code;
    private final String desc;

    PaymentStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaymentStatus fromCode(Integer code) {
        if (code == null) return null;
        for (PaymentStatus s : values()) {
            if (s.code.equals(code)) return s;
        }
        return null;
    }

    public static String descOf(Integer code) {
        PaymentStatus s = fromCode(code);
        return s == null ? null : s.desc;
    }
}
