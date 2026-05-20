package com.grocery.groceryshop.enums;

import lombok.Getter;

@Getter
public enum PayMethod {

    WECHAT(1, "微信支付"),
    ALIPAY(2, "支付宝"),
    BALANCE(3, "余额支付");

    private final Integer code;
    private final String desc;

    PayMethod(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayMethod fromCode(Integer code) {
        if (code == null) return null;
        for (PayMethod m : values()) {
            if (m.code.equals(code)) return m;
        }
        return null;
    }

    public static String descOf(Integer code) {
        PayMethod m = fromCode(code);
        return m == null ? null : m.desc;
    }
}
