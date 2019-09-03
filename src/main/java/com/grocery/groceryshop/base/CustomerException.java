package com.grocery.groceryshop.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerException extends RuntimeException {

  private String code; // 异常对应的返回码
  private String message; // 异常对应的描述信息
}
