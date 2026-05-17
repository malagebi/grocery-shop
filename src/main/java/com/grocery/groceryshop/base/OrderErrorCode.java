package com.grocery.groceryshop.base;

/** 订单模块业务错误码。消息文案以 messageKey 形式存放在 i18n 资源文件中。 */
public enum OrderErrorCode implements IErrorCode {

    ORDER_NOT_FOUND("404", "order.error.not_found"),
    ORDER_CANCELLED_NOT_MODIFIABLE("400", "order.error.cancelled_not_modifiable"),
    ORDER_COMPLETED_NOT_MODIFIABLE("400", "order.error.completed_not_modifiable"),
    ORDER_ALREADY_CANCELLED("400", "order.error.already_cancelled"),
    ORDER_STATUS_NOT_CANCELLABLE("400", "order.error.status_not_cancellable");

    private final String code;
    private final String messageKey;

    OrderErrorCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
}
