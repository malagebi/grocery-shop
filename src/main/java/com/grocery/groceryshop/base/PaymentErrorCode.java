package com.grocery.groceryshop.base;

public enum PaymentErrorCode implements IErrorCode {

    PAYMENT_NOT_FOUND("404", "payment.error.not_found"),
    ORDER_NOT_FOUND("404", "payment.error.order_not_found"),
    ORDER_NOT_UNPAID("400", "payment.error.order_not_unpaid"),
    PAYMENT_NOT_SUCCESS("400", "payment.error.not_success"),
    ALREADY_REFUNDED("400", "payment.error.already_refunded"),
    ORDER_STATUS_NOT_REFUNDABLE("400", "payment.error.order_status_not_refundable");

    private final String code;
    private final String messageKey;

    PaymentErrorCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessageKey() { return messageKey; }
}
