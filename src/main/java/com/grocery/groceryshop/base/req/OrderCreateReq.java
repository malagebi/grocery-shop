package com.grocery.groceryshop.base.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("创建订单请求")
public class OrderCreateReq {

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不允许为空")
    private Long userId;

    @ApiModelProperty(value = "商品ID", required = true)
    @NotNull(message = "商品ID不允许为空")
    private Long commodityId;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotNull(message = "商品名称不允许为空")
    private String commodityName;

    @ApiModelProperty(value = "购买数量", required = true)
    @NotNull(message = "购买数量不允许为空")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;

    @ApiModelProperty(value = "订单总金额", required = true)
    @NotNull(message = "订单总金额不允许为空")
    private BigDecimal totalAmount;
}
