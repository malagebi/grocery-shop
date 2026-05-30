package com.grocery.groceryshop.base.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "发起支付请求")
public class PayReq {

    @Schema(description = "订单号", required = true)
    @NotBlank(message = "订单号不允许为空")
    private String orderNo;

    @Schema(description = "支付方式: 1-微信 2-支付宝 3-余额", required = true)
    @NotNull(message = "支付方式不允许为空")
    private Integer payMethod;
}
