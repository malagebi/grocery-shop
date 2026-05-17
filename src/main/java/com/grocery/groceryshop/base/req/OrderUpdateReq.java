package com.grocery.groceryshop.base.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("修改订单请求")
public class OrderUpdateReq {

    @ApiModelProperty(value = "订单ID", required = true)
    @NotNull(message = "订单ID不允许为空")
    private Long id;

    @ApiModelProperty(value = "购买数量")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "订单状态: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消")
    private Integer status;
}
