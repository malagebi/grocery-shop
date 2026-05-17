package com.grocery.groceryshop.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerException extends RuntimeException {

  private String code; // 异常对应的返回码
  private String message; // 异常对应的描述信息

  /** 基于错误码枚举构造，文案按当前请求 Locale 从 i18n 资源中解析。 */
  public CustomerException(IErrorCode errorCode, Object... args) {
    this.code = errorCode.getCode();
    this.message = I18nUtil.get(errorCode.getMessageKey(), args);
  }
}
