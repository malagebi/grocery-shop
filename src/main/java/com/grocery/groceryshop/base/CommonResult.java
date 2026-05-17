package com.grocery.groceryshop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通用返回实体")
public class CommonResult<T> {

  @ApiModelProperty("消息")
  private String message;

  @ApiModelProperty("编码")
  private String code;

  @ApiModelProperty("结果")
  private T data;

  public static <T> CommonResult<T> success() {
    return new CommonResult<>();
  }

  public static <T> CommonResult<T> success(T data) {
    CommonResult<T> result = new CommonResult<>();
    result.setCode("ok");
    result.setMessage("成功");
    result.setData(data);
    return result;
  }

  public static <T> CommonResult<T> error(String message) {
    return error("fail", message);
  }

  public static <T> CommonResult<T> error(String code, String message) {
    CommonResult<T> result = new CommonResult<>();
    result.setCode(code);
    result.setMessage(message);
    return result;
  }
}
