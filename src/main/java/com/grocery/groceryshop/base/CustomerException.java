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
}
