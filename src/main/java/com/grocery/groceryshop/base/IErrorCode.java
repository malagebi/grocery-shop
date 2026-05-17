package com.grocery.groceryshop.base;

/** 业务错误码枚举需实现的接口，便于 {@link CustomerException} 统一处理。 */
public interface IErrorCode {

    /** 错误码（用于响应体的 code 字段）。 */
    String getCode();

    /** 国际化资源文件中的消息 key。 */
    String getMessageKey();
}
