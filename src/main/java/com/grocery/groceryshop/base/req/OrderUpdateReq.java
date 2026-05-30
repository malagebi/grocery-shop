package com.grocery.groceryshop.base.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "修改订单请求")
public class OrderUpdateReq {

    @Schema(description = "订单ID", required = true)
    @NotNull(message = "订单ID不允许为空")
    private Long id;

    @Schema(description = "购买数量")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "订单状态: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消")
    private Integer status;
}
