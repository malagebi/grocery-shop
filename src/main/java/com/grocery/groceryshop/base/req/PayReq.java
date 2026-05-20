package com.grocery.groceryshop.base.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("发起支付请求")
public class PayReq {

    @ApiModelProperty(value = "订单号", required = true)
    @NotBlank(message = "订单号不允许为空")
    private String orderNo;

    @ApiModelProperty(value = "支付方式: 1-微信 2-支付宝 3-余额", required = true)
    @NotNull(message = "支付方式不允许为空")
    private Integer payMethod;
}
