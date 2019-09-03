package com.grocery.groceryshop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel(value = "通用返回实体")
public class CommonResult<T> {

  /** 自定义操作信息 */
  @ApiModelProperty(value = "消息")
  private String message;

  /** 处理结果代码 */
  @ApiModelProperty(value = "编码")
  private String code;

  @ApiModelProperty(value = "结果")
  private T data;

  public static CommonResult success() {
    CommonResult result = new CommonResult();
    return result;
  }

  public static <T> CommonResult<T> success(T data) {
    CommonResult result = new CommonResult();
    result.setCode("ok");
    result.setMessage("成功");
    result.setData(data);
    return result;
  }

  public static <T> CommonResult<T> success(Collection<T> data) {
    CommonResult result = new CommonResult();
    result.setCode("ok");
    result.setMessage("成功");
    result.setData(data);
    return result;
  }

  public static <T> CommonResult<T> success(CommonPageInfo data) {
    CommonResult result = new CommonResult();
    result.setCode("ok");
    result.setMessage("成功");
    result.setData(data);
    return result;
  }

  public static CommonResult error(String errorMes) {
    CommonResult result = new CommonResult();
    result.setCode("fail");
    result.setMessage(errorMes);
    return result;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
