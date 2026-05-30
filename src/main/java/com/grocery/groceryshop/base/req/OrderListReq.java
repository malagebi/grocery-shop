package com.grocery.groceryshop.base.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单分页查询请求")
public class OrderListReq extends PageReq {

    @Schema(description ="用户ID")
    private Long userId;

    @Schema(description ="订单状态: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消")
    private Integer status;
}
