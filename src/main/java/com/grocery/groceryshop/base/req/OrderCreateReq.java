package com.grocery.groceryshop.base.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "创建订单请求")
public class OrderCreateReq {

    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不允许为空")
    private Long userId;

    @Schema(description = "商品ID", required = true)
    @NotNull(message = "商品ID不允许为空")
    private Long commodityId;

    @Schema(description = "商品名称", required = true)
    @NotNull(message = "商品名称不允许为空")
    private String commodityName;

    @Schema(description = "购买数量", required = true)
    @NotNull(message = "购买数量不允许为空")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;

    @Schema(description = "订单总金额", required = true)
    @NotNull(message = "订单总金额不允许为空")
    private BigDecimal totalAmount;
}
